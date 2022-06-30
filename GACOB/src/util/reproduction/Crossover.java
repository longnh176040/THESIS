package util.reproduction;

import java.util.ArrayList;

import core.Individual;
import core.Settings;

public class Crossover {
    public static ArrayList<Individual> SBXCrossover(Individual parent1, Individual parent2) {
        ArrayList<Individual> offspring = new ArrayList<Individual>();

        //Tính giá trị beta cho từng phần tử
        double betas[] = new double[parent1.chromosomeLength];
        for (int i = 0; i < betas.length; i++) {
            double u = Settings.random.nextDouble();
            if(u <= 0.5) {
                betas[i] = Math.pow((2*u), 1.0/(Settings.SBX_nc + 1));
            } else {
                betas[i] = Math.pow((2*(1-u)), -1.0/(Settings.SBX_nc + 1));
            }
        }

        //Tạo 2 cá thể mới
        Individual child1 = new Individual(parent1.chromosomeLength);
        Individual child2 = new Individual(parent2.chromosomeLength);

        for (int i = 0; i < betas.length; i++) {
            double v = 0.5*((1+betas[i]) * parent1.chromosome[i] + (1-betas[i]) * parent2.chromosome[i]);
            if(v > 1) v = 1 - Settings.SBX_MIN_LIMIT;
            else if(v < 0) v = Settings.SBX_MIN_LIMIT;
            child1.chromosome[i]= v;
            
            double v2 = 0.5*((1-betas[i]) * parent1.chromosome[i] + (1+betas[i]) * parent2.chromosome[i]);
            if(v2>1) v2= 1 - Settings.SBX_MIN_LIMIT;
            else if(v2<0) v2= Settings.SBX_MIN_LIMIT;
            child2.chromosome[i]= v2;
        }
        offspring.add(child1);
        offspring.add(child2);
        
        return offspring;
    }
}
