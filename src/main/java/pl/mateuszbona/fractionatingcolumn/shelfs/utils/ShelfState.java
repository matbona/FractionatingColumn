package pl.mateuszbona.fractionatingcolumn.shelfs.utils;

public class ShelfState {

	private double y;
	private double l;
	private double m;
	private double x;

	public double getY() {
		return y;
	}

	public double getL() {
		return l;
	}

	public double getM() {
		return m;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setL(double l) {
		this.l = l;
	}

	public void setM(double m) {
		this.m = m;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return "ShelfState [y=" + y + ", l=" + l + ", m=" + m + ", x=" + x + "]";
	}

	public static ShelfState getInstance() {
		return new ShelfState();
	}
}
