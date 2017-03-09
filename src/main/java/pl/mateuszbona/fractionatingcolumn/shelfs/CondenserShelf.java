package pl.mateuszbona.fractionatingcolumn.shelfs;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegralType;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.ShelfVariables;
import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnConstants;
import pl.mateuszbona.fractionatingcolumn.utils.InitialConditions;

public class CondenserShelf extends Shelf {

	public CondenserShelf(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		super(initialConditions, constants, integrationParams);
	}
	
	@Override
	public double calculateX(ShelfVariables variables) {
		double v = variables.getV();
		double y = variables.getY();
		
		double xd = state.getX();
		double md = state.getM();

		double result = (v * (y - xd)) / md;
		return eulerLimitedIntegeration(xd, result, IntegralType.X);
	}


	@Override
	public double calculateM(ShelfVariables variables) {
		double v = variables.getV();
		double r = variables.getR();
		double md = state.getM();

		double result = v - r - md;
		return eulerLimitedIntegeration(md, result, IntegralType.M);
	}

	public static CondenserShelf getInstance(InitialConditions initialConditions, FractionatingColumnConstants constants, IntegrationParams integrationParams) {
		return new CondenserShelf(initialConditions, constants, integrationParams);
	}
}
