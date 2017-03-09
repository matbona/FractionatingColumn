package pl.mateuszbona.fractionatingcolumn.control;

public class ControlSignal {

	private double v;
	private double r;

	public ControlSignal(double v, double r) {
		this.v = v;
		this.r = r;
	}

	public double getV() {
		return v;
	}

	public double getR() {
		return r;
	}

}
