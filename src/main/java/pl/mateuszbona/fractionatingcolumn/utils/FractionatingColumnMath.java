package pl.mateuszbona.fractionatingcolumn.utils;

public class FractionatingColumnMath {

	public static double molarToPercent(double x, double ma, double mb) {
		double result = (100 * x) / (mb / ma - x * (mb / ma - 1));

		return roundToThreeDigitsAfterComma(result);
	}

	private static double roundToThreeDigitsAfterComma(double value) {
		value *= 1000;
		value = Math.round(value);
		value /= 1000;

		return value;
	}

}
