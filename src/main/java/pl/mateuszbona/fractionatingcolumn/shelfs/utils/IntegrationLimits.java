package pl.mateuszbona.fractionatingcolumn.shelfs.utils;

public class IntegrationLimits {
    private double xLowerLimit;
    private double xHigherLimit;
    private double mLowerLimit;
    private double mHigherLimit;

    public IntegrationLimits(double xLowerLimit, double xHigherLimit, double mLowerLimit, double mHigherLimit) {
        this.xLowerLimit = xLowerLimit;
        this.xHigherLimit = xHigherLimit;
        this.mLowerLimit = mLowerLimit;
        this.mHigherLimit = mHigherLimit;
    }

    public double getxLowerLimit() {
        return xLowerLimit;
    }

    public double getxHigherLimit() {
        return xHigherLimit;
    }

    public double getmLowerLimit() {
        return mLowerLimit;
    }

    public double getmHigherLimit() {
        return mHigherLimit;
    }
}
