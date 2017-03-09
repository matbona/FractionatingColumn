package pl.mateuszbona.fractionatingcolumn.utils;

public class FractionatingColumnConstants {

	private final double ALPHA;
	private final double BETA;
	private final double M0;
	private final double L0;

	public FractionatingColumnConstants(double alpha, double beta, double m0, double l0) {
		this.ALPHA = alpha;
		this.BETA = beta;
		this.M0 = m0;
		this.L0 = l0;
	}

	public double getAplha() {
		return ALPHA;
	}

	public double getBeta() {
		return BETA;
	}

	public double getM0() {
		return M0;
	}

	public double getL0() {
		return L0;
	}

}
