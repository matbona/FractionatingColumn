package pl.mateuszbona.fractionatingcolumn.control;

import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnInput;

public class FeedforwardController {

	private double v = 1.0;
	private double r = 1.0;
	private boolean initV = true;
	private boolean initR = true;

	public ControlSignal control(FractionatingColumnInput input) {
		double f = input.getF();

		v = calculateV(f);
		r = calculateR(f);

		return new ControlSignal(v, r);
	}

	private double calculateV(double f) {
		if (initV) {
			initV = false;
		} else {
			double v1 = 0.6485 * v + f;
			v = v1;
		}

		return v;
	}

	private double calculateR(double f) {
		if (initR) {
			initR = false;
		} else {
			double r1 = -0.048 * r + 2.8393 * f;
			r = r1;
		}

		return r;
	}
}
