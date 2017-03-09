package pl.mateuszbona.fractionatingcolumn.control;

import pl.mateuszbona.fractionatingcolumn.utils.FractionatingColumnInput;

public class FeedforwardController {

	public ControlSignal control(FractionatingColumnInput input) {
		/*
		 * TODO: Should calculate control signal from input
		 */

		return new ControlSignal(113.8, 108.4);
	}
}
