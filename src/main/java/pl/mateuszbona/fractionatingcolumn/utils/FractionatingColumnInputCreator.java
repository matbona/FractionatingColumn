package pl.mateuszbona.fractionatingcolumn.utils;

import java.util.ArrayList;
import java.util.List;

public class FractionatingColumnInputCreator {
    public static List<FractionatingColumnInput> create(double f, double xf, double delta, double time) {
        List<FractionatingColumnInput> result = new ArrayList<>();
        int stepSize = (int) (1 / delta);

        for (int i = 0; i < stepSize * time; i++) {
            result.add(new FractionatingColumnInput(f, xf));
        }

        return result;
    }
}
