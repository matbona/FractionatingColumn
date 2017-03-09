package pl.mateuszbona.fractionatingcolumn.utils;

public class FractionatingColumnParams {

	private final int shelfCount;
	private final int feederIndex;

	public FractionatingColumnParams(int shelfCount, int feederIndex) {
		this.shelfCount = shelfCount;
		this.feederIndex = feederIndex;
	}

	public int getShelfCount() {
		return shelfCount;
	}

	public int getFeederIndex() {
		return feederIndex;
	}

}
