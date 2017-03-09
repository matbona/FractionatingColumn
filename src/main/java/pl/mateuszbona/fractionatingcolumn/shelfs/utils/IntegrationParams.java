package pl.mateuszbona.fractionatingcolumn.shelfs.utils;

public class IntegrationParams {
    private double delta;
    private IntegrationLimits integrationLimits;

    public IntegrationParams(double delta, IntegrationLimits integrationLimits) {
        this.delta = delta;
        this.integrationLimits = integrationLimits;
    }

    public double getDelta() {
        return delta;
    }

    public IntegrationLimits getIntegrationLimits() {
        return integrationLimits;
    }
}
