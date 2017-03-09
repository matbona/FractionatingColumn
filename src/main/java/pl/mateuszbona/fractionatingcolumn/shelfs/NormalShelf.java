package pl.mateuszbona.fractionatingcolumn.shelfs;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegralType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnConstants;
import pl.mateuszbona.fractionatingcolumn.utils.InitialConditions;

public class NormalShelf extends Shelf {

	public NormalShelf(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		super(initialConditions, constants, integrationParams);
	}

	@Override
	public double calculateX(ShelfVariables variables) {
		double l = variables.getL();
		double x = variables.getX();
		double v = variables.getV();
		double y = variables.getY();

		double xFromActualShelf = state.getX();
		double yFromActualShelf = state.getY();
		double mFromActualShelf = state.getM();

		double result = (l * (x - xFromActualShelf) + v * (y - yFromActualShelf)) / mFromActualShelf;
		return eulerLimitedIntegeration(xFromActualShelf, result, IntegralType.X);
	}

	@Override
	public double calculateM(ShelfVariables variables) {
		double lFromHigerShelf = variables.getL();
		double lFromActualShelf = state.getL();
		
		double m = state.getM();

		double result = lFromHigerShelf - lFromActualShelf;
		return eulerLimitedIntegeration(m, result, IntegralType.M);
	}

	public static NormalShelf getInstance(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		return new NormalShelf(initialConditions, constants, integrationParams);
	}

}
