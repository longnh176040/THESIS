package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import util.reproduction.Crossover;
import util.reproduction.Mutation;

public class Population {
    public int popSize; //Kích thước quần thể
    public ArrayList<Individual> population;
    Task task;
    int chromosomeLength = -1;

    Random random;

    public Population(Task task, int popSize) {
        this.popSize = popSize;
        this.task = task;
        
        this.chromosomeLength = task.domainNumber;
        this.population = new ArrayList<Individual>();

        random = Settings.random;
    }

    public void InitPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            Individual individual = new Individual(chromosomeLength);
            individual.RandomInit();
            individual.EvaluateFitness(task);
            population.add(individual);
        }
    }

    public ArrayList<Individual> Reproduction(Population population) {
        ArrayList<Individual> offspringPop = new ArrayList<Individual>();

        //List sử dụng cho việc shuffle + random + ....
        ArrayList<Integer> randList = new ArrayList<Integer>();

        for (int i = 0; i < population.popSize; i++) {
            randList.add(i);
        }

        Collections.shuffle(randList, Settings.random);

        while (offspringPop.size() < population.popSize) {
            //Lấy ngẫu nhiên 2 cha mẹ trong quần thể (ko trùng)
            int a = randList.get(random.nextInt(population.popSize / 2));
            int b = randList.get(random.nextInt(population.popSize / 2) + population.popSize/2);
            Individual parent1 = population.population.get(a);
            Individual parent2 = population.population.get(b);
            ArrayList<Individual> offspring; //List cá thể con đc sinh ra từ 2 cha mẹ

            offspring = Crossover.SBXCrossover(parent1, parent2);
            for (Individual individual : offspring) {
                if (random.nextFloat() <= Settings.MUTATION_RATE)
                Mutation.PolynomialMutation(individual);
            }

            //population.SwapIndividuals(offspring.get(0), offspring.get(1));
            //Đánh giá các cá thể với tác vụ
            for (Individual individual : offspring) {
                individual.EvaluateFitness(task);
            }
            
            offspringPop.addAll(offspring);
        }
        
        return offspringPop;
    }

    public void Selection() {
        //Sắp xếp quần thể theo fitness giảm dần
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return -Double.valueOf(o1.fitness).compareTo(o2.fitness);
            }
        });

        int size = population.size();

        //Giữ lại số cá thể = kích thước quần thể
        if(population.size()> Settings.GA_POPULATION_SIZE) 
            population.subList(Settings.GA_POPULATION_SIZE, size).clear();  

        // for (Individual indiv: this.population) {
        //     System.out.println(indiv.scalar_fitness);
        // }
    }

    public void SwapIndividuals(Individual i1, Individual i2) {
        for(int i=1; i <= i1.chromosomeLength; i++){
            if (Settings.random.nextDouble() > 0.5){
                double temp1 = i1.chromosome[i-1];
                double temp2 = i2.chromosome[i-1];
                i1.chromosome[i-1] = temp2;
                i2.chromosome[i-1] = temp1;
            }
        }
    }
}
