package pl.mateuszbona.fractionatingcolumn;

import pl.mateuszbona.fractionatingcolumn.control.ControlSignal;
import pl.mateuszbona.fractionatingcolumn.control.FeedforwardController;
import pl.mateuszbona.fractionatingcolumn.shelfs.*;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnMath.molarToPercent;

public class FractionatingColumn {

	private final int EVAPORATOR_INDEX = 0;
	private final int FIRST_SHELF_INDEX = 1;
	private final int HIGHEST_SHELF_INDEX;
	private final int FEEDER_INDEX;
	private final int CONDENSER_INDEX;
	private final int SHELF_COUNT;

	private final double ATOMIC_WEIGHT_OF_WATER = 18;
	private final double ATOMIC_WEIGHT_OF_ACETIC_ACID = 60.05;
	
	private List<Shelf> shelfs = new ArrayList<>();
	private IntegrationParams integrationParams;

	public FractionatingColumn(FractionatingColumnParams parameters, FractionatingColumnConstants constants, List<InitialConditions> initialConditions, IntegrationParams integrationParams) {
		this.SHELF_COUNT = parameters.getShelfCount();
		this.FEEDER_INDEX = parameters.getFeederIndex();
		this.CONDENSER_INDEX = SHELF_COUNT + 1;
		this.HIGHEST_SHELF_INDEX = SHELF_COUNT;
		this.integrationParams = integrationParams;
		createShelfs(constants, initialConditions, integrationParams);
	}

	public List<FractionatingColumnOutput> startSimulation(int time, List<FractionatingColumnInput> input) {
		FeedforwardController feedforwardController = new FeedforwardController();
		ControlSignal controlSignal;
		List<FractionatingColumnOutput> output = new ArrayList<>();
		int stepSize = (int) (1 / integrationParams.getDelta());
		double xd, xb;

		for (int t = 0; t < stepSize * time; t++) {
			controlSignal = feedforwardController.control(input.get(t));
			updateStateForAllShelfs(input.get(t), controlSignal);

			xd = molarToPercent(shelfs.get(CONDENSER_INDEX).getState().getX(), ATOMIC_WEIGHT_OF_WATER, ATOMIC_WEIGHT_OF_ACETIC_ACID);
			xb = molarToPercent(shelfs.get(EVAPORATOR_INDEX).getState().getX(), ATOMIC_WEIGHT_OF_WATER, ATOMIC_WEIGHT_OF_ACETIC_ACID);

			if (t % stepSize == 0 || t == 0) {
				output.add(new FractionatingColumnOutput(xd, xb));
			}
		}

		return output;
	}

	public void saveToCSV(String filename, int time, List<FractionatingColumnOutput> output) {
		String toSave = makeCSVOutput(time, output);
		Path path = Paths.get(filename);

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(toSave);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createShelfs(FractionatingColumnConstants constants, List<InitialConditions> initialConditions, IntegrationParams integrationParams) {
		shelfs.add(EvaporatorShelf.getInstance(initialConditions.get(0), constants, integrationParams));

		for (int i = 1; i <= SHELF_COUNT; i++) {
			if (i == FEEDER_INDEX) {
				shelfs.add(FeederShelf.getInstance(initialConditions.get(i), constants, integrationParams));
			} else {
				shelfs.add(NormalShelf.getInstance(initialConditions.get(i), constants, integrationParams));
			}
		}

		shelfs.add(CondenserShelf.getInstance(initialConditions.get(SHELF_COUNT + 1), constants, integrationParams));
	}

	private String makeCSVOutput(int time, List<FractionatingColumnOutput> output) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < time; i++) {
			result.append(i);
			result.append(",");

			result.append(output.get(i).getXd());
			result.append(",");

			result.append(output.get(i).getXb());
			result.append("\n");
		}

		return result.toString();
	}

	private void updateStateForAllShelfs(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
		UpdateStateForAllShelfs updateStateForAllShelfs = new UpdateStateForAllShelfs();
		
		updateStateForAllShelfs.update(fractionatingColumnInput, controlSignal);
	}

	private class UpdateStateForAllShelfs {
		
		public void update(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
			updateX(fractionatingColumnInput, controlSignal);
			updateM(fractionatingColumnInput, controlSignal);
			updateY();
			updateL();
		}

		private void updateX(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
			updateXForFeeder(fractionatingColumnInput, controlSignal);
			updateXForNormalShelfsUnderFeeder(controlSignal);
			updateXForEvaporatorShelf(controlSignal);
			updateXForNormalShelfsAboveFeeder(controlSignal);
			updateXForCondenserShelf(controlSignal);
		}
		
		private void updateM(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
			updateMForEvaporatorShelf(controlSignal);
			updateMForNormalAndFeederShelf(fractionatingColumnInput, controlSignal);
			updateMForCondenserShelf(controlSignal);
		}
		
		private void updateY() {
			for (int i = EVAPORATOR_INDEX; i <= HIGHEST_SHELF_INDEX; i++) {
				shelfs.get(i).updateStateY();
			}
		}
		
		private void updateL() {
			for (int i = FIRST_SHELF_INDEX; i <= HIGHEST_SHELF_INDEX; i++) {
				shelfs.get(i).updateStateL();
			}
		}
		
		private void updateXForFeeder(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
			double l = shelfs.get(FEEDER_INDEX + 1).getState().getL();
			double x = shelfs.get(FEEDER_INDEX + 1).getState().getX();
			double v = controlSignal.getV();
			double y = shelfs.get(FEEDER_INDEX - 1).getState().getY(); 
			double f = fractionatingColumnInput.getF();
			double xf = fractionatingColumnInput.getXF();
			
			ShelfVariables variables = ShelfVariables.getInstance(l, x, v, y, f, xf);
			shelfs.get(FEEDER_INDEX).updateStateX(variables);
		}
		
		private void updateXForNormalShelfsUnderFeeder(ControlSignal controlSignal) {
			double l;
			double x;
			double v = controlSignal.getV();
			double y;
			ShelfVariables variables;
			
			for (int i = FEEDER_INDEX - 1; i >= 1; i--) {
				l = shelfs.get(i + 1).getState().getL();
				x = shelfs.get(i + 1).getXFromPreviousState();
				y = shelfs.get(i - 1).getState().getY();
				
				variables = ShelfVariables.getInstance(l, x, v, y);
				
				shelfs.get(i).updateStateX(variables);
			}
		}
		
		private void updateXForEvaporatorShelf(ControlSignal controlSignal) {
			double l = shelfs.get(FIRST_SHELF_INDEX).getState().getL();
			double x = shelfs.get(FIRST_SHELF_INDEX).getState().getX();
			double v = controlSignal.getV();
			ShelfVariables variables  = ShelfVariables.getInstance(l, x, v);
			
			shelfs.get(EVAPORATOR_INDEX).updateStateX(variables);			
		}
		
		private void updateXForNormalShelfsAboveFeeder(ControlSignal controlSignal) {
			double l;
			double x;
			double v = controlSignal.getV();
			double y;
			ShelfVariables variables;
			
			for (int i = FEEDER_INDEX + 1; i <= HIGHEST_SHELF_INDEX; i++) {
				if (i == HIGHEST_SHELF_INDEX) {
					l = controlSignal.getR();
				} else {
					l = shelfs.get(i + 1).getState().getL();

				}
				
				x = shelfs.get(i + 1).getState().getX();
				y = shelfs.get(i - 1).getState().getY();
				
				variables = ShelfVariables.getInstance(l, x, v, y);
				
				shelfs.get(i).updateStateX(variables);
			}
		}
		
		private void updateXForCondenserShelf(ControlSignal controlSignal) {
			double v = controlSignal.getV();
			double y = shelfs.get(HIGHEST_SHELF_INDEX).getState().getY();
			ShelfVariables variables = ShelfVariables.getInstance(0, 0, v, y);
			
			shelfs.get(CONDENSER_INDEX).updateStateX(variables);
		}

		private void updateMForEvaporatorShelf(ControlSignal controlSignal) {
			double l = shelfs.get(FIRST_SHELF_INDEX).getState().getL();
			double v = controlSignal.getV();
			ShelfVariables variables = ShelfVariables.getInstance(l, v, ShelfType.EVAPORATOR);
			
			shelfs.get(EVAPORATOR_INDEX).updateStateM(variables);
		}
		
		private void updateMForNormalAndFeederShelf(FractionatingColumnInput fractionatingColumnInput, ControlSignal controlSignal) {
			double l;
			ShelfVariables variables;
			
			for (int i = FIRST_SHELF_INDEX; i <= HIGHEST_SHELF_INDEX; i++) {
				if (i == FEEDER_INDEX) {
					double f = fractionatingColumnInput.getF();
					
					l = shelfs.get(i + 1).getState().getL();
					variables = ShelfVariables.getInstance(l, f, ShelfType.FEEDER);
					shelfs.get(i).updateStateM(variables);
					continue;
				}
				
				if (i == HIGHEST_SHELF_INDEX) {
					l = controlSignal.getR();
				} else {
					l = shelfs.get(i + 1).getState().getL();
				}
				
				variables = ShelfVariables.getInstance(l);
				shelfs.get(i).updateStateM(variables);
			}
		}
		
		private void updateMForCondenserShelf(ControlSignal controlSignal) {
			double v = controlSignal.getV();
			double r = controlSignal.getR();
			ShelfVariables variables = ShelfVariables.getInstance(v, r, ShelfType.CONDENSER);
			
			shelfs.get(CONDENSER_INDEX).updateStateM(variables);
		}

	}
}
