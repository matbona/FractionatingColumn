package pl.mateuszbona.fractionatingcolumn.shelfs;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegralType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnConstants;
import pl.mateuszbona.fractionatingcolumn.utils.InitialConditions;

public class EvaporatorShelf extends Shelf {

	public EvaporatorShelf(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		super(initialConditions, constants, integrationParams);
	}

	@Override
	public double calculateX(ShelfVariables variables) {
		double l = variables.getL();
		double x = variables.getX();
		double v = variables.getV();
		
		double xb = state.getX();
		double yb = state.getY();
		double mb = state.getM();

		double result = (l * (x - xb) + v * (xb - yb)) / mb;
		return eulerLimitedIntegeration(xb, result, IntegralType.X);
	}
	
	@Override
	public double calculateM(ShelfVariables variables) {
		double l = variables.getL();
		double v = variables.getV();
		double mb = state.getM();

		double result = l - v - mb;
		return eulerLimitedIntegeration(mb, result, IntegralType.M);
	}

	public static EvaporatorShelf getInstance(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		return new EvaporatorShelf(initialConditions, constants, integrationParams);
	}

}
