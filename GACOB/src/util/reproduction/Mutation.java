package util.reproduction;

import core.Individual;
import core.Settings;

public class Mutation {
    public static void PolynomialMutation(Individual ind) {
        for(int i=1; i <= ind.chromosomeLength; i++){
            if(Settings.random.nextDouble() < 1.0/ind.chromosomeLength) {
                double u = Settings.random.nextDouble();
                double v = 0;
                if(u <= 0.5){
                    double del = Math.pow((2*u), 1.0/(1 + Settings.PM_nm)) - 1;
                    v = ind.chromosome[i-1]*(del+1);
                }else{
                   double del= 1 - Math.pow(2*(1-u), 1.0/(1 + Settings.PM_nm));
                   v = ind.chromosome[i-1] +del*(1-ind.chromosome[i-1]);
                }
                if(v > 1) ind.chromosome[i-1] = ind.chromosome[i-1] + Settings.random.nextDouble() * (1-ind.chromosome[i-1]);
                else if(v < 0) ind.chromosome[i-1] = ind.chromosome[i-1] * Settings.random.nextDouble();
            }
            
        }
    }

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
