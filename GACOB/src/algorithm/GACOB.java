package algorithm;

import java.util.ArrayList;
import java.util.Random;

import core.Individual;
import core.Population;
import core.Settings;
import core.Task;

public class GACOB {
    Task task;

    int popSize;    //Kích thước quần thể khởi tạo
    Random random;

    int generation = 0; //Biến dùng để đếm số thế hệ

    public GACOB(Task task, int popSize) {
        this.task = task;
        this.popSize = popSize;

        random = Settings.random;
    }

    public Individual run (int seed, double[] convergenceTrend) {
        long start =  System.currentTimeMillis();

        random = new Random(seed);
        Individual bestIndividuals;

        //Khởi tạo quần thể đầu tiên
        Population population = new Population(task, popSize);
        population.InitPopulation();

        population.Selection();

        generation = 1;
       
        bestIndividuals = population.population.get(0);
        //Cập nhật các cá thể có fitness lớn nhất
        convergenceTrend[generation - 1] = bestIndividuals.fitness;
        
        while (generation < Settings.GA_GENERATION) {
            generation++;

            System.out.println("===============generation: " + generation + "=================");
            ArrayList<Individual> offspringPopulation = population.Reproduction(population);
            population.population.addAll(offspringPopulation);
            //System.out.println("pop_size: " + population.population.size());
            population.Selection();

            bestIndividuals = population.population.get(0);
            //Cập nhật các cá thể có fitness lớn nhất
            convergenceTrend[generation - 1] = bestIndividuals.fitness;

            System.out.println("Best individual " + population.population.get(0).cost);
        }

        System.out.println("Seed " + seed);
        System.out.println("Skill factor: " + bestIndividuals + 
        " Fitness: " + bestIndividuals.fitness +
        " Cost: " + bestIndividuals.cost
        );

        
        long end =  System.currentTimeMillis();
        System.out.println(end-start);

        return bestIndividuals;
    }

    
}
