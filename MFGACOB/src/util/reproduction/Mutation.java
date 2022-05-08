package util.reproduction;

import core.Individual;
import core.Settings;

public class Mutation {

    public static void GaussMutation(Individual ind) {
        for (int i = 0; i < ind.chromosomeLength; i++) {
            if (Settings.random.nextDouble() < 1.0 / ind.chromosomeLength) {
                double t = ind.chromosome[i] + Settings.random.nextGaussian();
                if (t > 1) {
                    t = ind.chromosome[i] + Settings.random.nextDouble() * (1 - ind.chromosome[i]);
                } else if (t < 0) {
                    t = Settings.random.nextDouble() * ind.chromosome[i];
                }

                ind.chromosome[i] = t;
            }
        }
    }
}
