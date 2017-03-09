package pl.mateuszbona.fractionatingcolumn.shelfs.utils;

public class ShelfVariables {

	private double l;
	private double x;
	private double v;
	private double r;
	private double y;
	private double f;
	private double xf;

	private ShelfVariables(double l) {
		this.l = l;
	}

	private ShelfVariables(double llv, double fvr, ShelfType shelfType) {
		switch (shelfType) {
		case FEEDER:
			this.l = llv;
			this.f = fvr;
			break;

		case EVAPORATOR:
			this.l = llv;
			this.v = fvr;
			break;

		case CONDENSER:
			this.v = llv;
			this.r = fvr;

		default:
			break;
		}
	}

	private ShelfVariables(double l, double x, double v) {
		this.l = l;
		this.x = x;
		this.v = v;
	}
	
	private ShelfVariables(double l, double x, double v, double y) {
		this.l = l;
		this.x = x;
		this.v = v;
		this.y = y;
	}

	private ShelfVariables(double l, double x, double v, double y, double f, double xf) {
		this.l = l;
		this.x = x;
		this.v = v;
		this.y = y;
		this.f = f;
		this.xf = xf;
	}

	public double getL() {
		return l;
	}

	public double getX() {
		return x;
	}

	public double getV() {
		return v;
	}

	public double getR() {
		return r;
	}

	public double getY() {
		return y;
	}

	public double getF() {
		return f;
	}

	public double getXf() {
		return xf;
	}

	public static ShelfVariables getInstance(double l) {
		return new ShelfVariables(l);
	}

	public static ShelfVariables getInstance(double llv, double fvr, ShelfType shelfType) {
		return new ShelfVariables(llv, fvr, shelfType);
	}

	public static ShelfVariables getInstance(double l, double x, double v) {
		return new ShelfVariables(l, x, v);
	}
	
	public static ShelfVariables getInstance(double l, double x, double v, double y) {
		return new ShelfVariables(l, x, v, y);
	}

	public static ShelfVariables getInstance(double l, double x, double v, double y, double f, double xf) {
		return new ShelfVariables(l, x, v, y, f, xf);
	}
}
