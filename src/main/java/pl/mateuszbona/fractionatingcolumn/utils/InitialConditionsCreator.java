package pl.mateuszbona.fractionatingcolumn.utils;

import java.util.ArrayList;
import java.util.List;

public class InitialConditionsCreator {
    public static List<InitialConditions> create(double initialM, double initialX, int count) {
        List<InitialConditions> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(new InitialConditions(initialM, initialX));
        }

        return  result;
    }
}
