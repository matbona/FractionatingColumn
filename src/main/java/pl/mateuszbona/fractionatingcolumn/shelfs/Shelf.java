package pl.mateuszbona.fractionatingcolumn.shelfs;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegralType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfState;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnConstants;
import pl.mateuszbona.fractionatingcolumn.utils.InitialConditions;

public abstract class Shelf {

	protected final FractionatingColumnConstants constants;
	protected ShelfState state;
	private IntegrationParams integrationParams;
	private double xFromPreviousState;
	private boolean initStateFlag;

	public Shelf(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		this.constants = constants;
		this.integrationParams = integrationParams;
		this.state = ShelfState.getInstance();
		this.initStateFlag = false;
		initializeState(initialConditions);
	}

	public double calculateY() {
		double x = state.getX();
		double alpha = constants.getAplha();

		return (alpha * x) / (1 + (alpha - 1) * x);

	}
	
	public double calculateL() {
		double m = state.getM();
		double m0 = constants.getM0();
		double beta = constants.getBeta();
		double l0 = constants.getL0();

		return ((m - m0) / beta) + l0;	
	}
	
	public abstract double calculateX(ShelfVariables variables);
	
	public abstract double calculateM(ShelfVariables variables);
	
	public void updateStateY() {
		state.setY(calculateY());
	}

	public void updateStateL() {
		state.setL(calculateL());
	}
	
	public void updateStateX(ShelfVariables variables) {
		xFromPreviousState = state.getX();
		state.setX(calculateX(variables));
	}

	public void updateStateM(ShelfVariables variables) {
		state.setM(calculateM(variables));
	}

	public ShelfState getState() {
		return state;
	}
	
	public double getXFromPreviousState() {
		return xFromPreviousState;
	}

	protected double eulerLimitedIntegeration(double actualValueOfX, double derivativeOfX, IntegralType integralType) {
		double result = actualValueOfX + integrationParams.getDelta() * derivativeOfX;

		return limitIntegrationResult(result, integralType);
	}

	private void initializeState(InitialConditions initialConditions) {
		if (!initStateFlag) {
			
			state.setX(initialConditions.getX());
			state.setM(initialConditions.getM());
			
			if (this instanceof EvaporatorShelf) {	
				state.setY(calculateY());
			} else if (this instanceof NormalShelf || this instanceof FeederShelf) {
				state.setY(calculateY());
				state.setL(calculateL());
			}
			
			initStateFlag = true;
		}
	}

	private double limitIntegrationResult(double integrationResult, IntegralType integralType) {
		double result = integrationResult;

		switch (integralType) {
			case X:
				double xLowerLimit = integrationParams.getIntegrationLimits().getxLowerLimit();
				double xHigherLimit = integrationParams.getIntegrationLimits().getxHigherLimit();

				if (result < xLowerLimit) {
					result = xLowerLimit;
				} else if (result > xHigherLimit) {
					result = xHigherLimit;
				}

				break;

			case M:
				double mLowerLimit = integrationParams.getIntegrationLimits().getmLowerLimit();
				double mHigherLimit = integrationParams.getIntegrationLimits().getmHigherLimit();

				if (result < mLowerLimit) {
					result = mLowerLimit;
				} else if (result > mHigherLimit) {
					result = mHigherLimit;
				}

				break;
		}

		return result;
	}
}
