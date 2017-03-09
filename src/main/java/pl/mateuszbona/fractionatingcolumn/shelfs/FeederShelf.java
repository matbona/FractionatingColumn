package pl.mateuszbona.fractionatingcolumn.shelfs;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegralType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnConstants;
import pl.mateuszbona.fractionatingcolumn.utils.InitialConditions;

public class FeederShelf extends Shelf {

	public FeederShelf(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		super(initialConditions, constants, integrationParams);
	}
	
	@Override
	public double calculateX(ShelfVariables variables) {
		double l = variables.getL();
		double x = variables.getX();
		double v = variables.getV();
		double y = variables.getY();
		double f = variables.getF();
		double xf = variables.getXf();
		
		double xFromActualShelf = state.getX();
		double yFromActualShelf = state.getY();
		double mFromActualShelf = state.getM();
		
		double result = (l * (x - xFromActualShelf) + v * (y - yFromActualShelf) + f * (xf - xFromActualShelf)) / mFromActualShelf;
		return eulerLimitedIntegeration(xFromActualShelf, result, IntegralType.X);
	}

	@Override
	public double calculateM(ShelfVariables variables) {
		double lFromHigerShelf = variables.getL();
		double f = variables.getF();
		double lFromActualShelf = state.getL();

		double m = state.getM();

		double result = lFromHigerShelf - lFromActualShelf + f;
		return eulerLimitedIntegeration(m, result, IntegralType.M);
	}

	public static FeederShelf getInstance(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		return new FeederShelf(initialConditions, constants, integrationParams);
	}

}
