package pl.mateuszbona.fractionatingcolumn;

import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationLimits;
import pl.mateuszbona.fractionatingcolumn.shelfs.utils.IntegrationParams;
import pl.mateuszbona.fractionatingcolumn.utils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FractionatingColumnMain {

    public static void main(String[] args) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[dd-MM-yyyy_HH.mm.ss]");
        String dateToFileName = simpleDateFormat.format(new Date());

        String filename = "sim" + dateToFileName + ".csv";


        int time = 3600;
        int shelfCount = 38;
        int feederIndex = 9;
        double alpha = 1.51;
        double beta = 0.1;
        double m0 = 0.5;
        double l0 = 1;
        double initialM = 0.5;
        double initialX = 0;
        double f = 40;
        double xf = 0.15;
        double delta = 0.0005;

        double xLowerLimit = 0;
        double xHigherLimit = 1;
        double mLowerLimit = 0.5;
        double mHigherLimit = 18;
        IntegrationLimits integrationLimits = new IntegrationLimits(xLowerLimit, xHigherLimit, mLowerLimit, mHigherLimit);

        FractionatingColumnParams parameters = new FractionatingColumnParams(shelfCount, feederIndex);
        FractionatingColumnConstants constants = new FractionatingColumnConstants(alpha, beta, m0, l0);
        List<InitialConditions> initialConditions = InitialConditionsCreator.create(initialM, initialX, shelfCount + 2);
        IntegrationParams integrationParams = new IntegrationParams(delta, integrationLimits);

        FractionatingColumn fractionatingColumn = new FractionatingColumn(parameters, constants, initialConditions, integrationParams);

        List<FractionatingColumnInput> input = FractionatingColumnInputCreator.create(f, xf, delta, time);

        System.out.println("Starting simulation...");
        List<FractionatingColumnOutput> output = fractionatingColumn.startSimulation(time, input);

        System.out.println("Simulation results:\nxd = " + output.get(output.size() - 1).getXd() + "\nxb = " + output.get(output.size() - 1).getXb());

        System.out.println("Saving to file: " + filename);
        fractionatingColumn.saveToCSV(filename, time, output);
    }

}
