package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Population {
    public int popSize; //Kích thước quần thể
    public ArrayList<Individual> population;
    Task[] tasks;
    int chromosomeLength = -1;
    int taskNum;

    public Population(Task[] tasks, int popSize) {
        this.popSize = popSize;
        this.taskNum = tasks.length;
        this.tasks = tasks;
        for(Task t: tasks){
            this.chromosomeLength = Math.max(this.chromosomeLength, t.domainNumber);
        }
        this.population = new ArrayList<Individual>();
    }

    public void InitPopulation() {
        for (int i = 0; i < this.popSize; i++) {
            Individual individual = new Individual(taskNum, chromosomeLength);
            individual.RandomInit();
            int skill_factor = 1 + i % taskNum;
            individual.skill_factor = skill_factor;
            individual.EvaluateFitnessBestTask(tasks);
            //individual.EvaluateFitnessAllTasks(tasks);
            population.add(individual);
        }
    }

    public void UpdateRank() {
        for (int task = taskNum; task >= 1; task--) {
            final int t = task;
            //Sắp xếp fitness của quần thể theo thứ tự giảm dần ứng với từng tác vụ
            Collections.sort(population, new Comparator<Individual>() {
                @Override
                public int compare(Individual o1, Individual o2) {
                    return -Double.valueOf(o1.fitness[t-1]).compareTo(o2.fitness[t-1]);
                }
            });
            //Cập nhật ranking của các cá thể ứng với tác vụ đang xét
            for(int i = 0; i < population.size(); i++) {
                population.get(i).rank[task-1] = i;
            }
        }

        // for (Individual indiv: population) {
        //     System.out.println(indiv.rank[0] + " " + indiv.rank[1] + " " + indiv.fitness[0] + "\t" + indiv.fitness[1]);
        // }
    }

    public void UpdateScalarFitness() {
        for (Individual individual : population) {
            int minRank = Integer.MAX_VALUE;
            // int bestTask = 0;
            //Tìm kiếm tác vụ có rank cao nhất
            for (int task = 1; task <= tasks.length; task++) {
                if (minRank > individual.rank[task-1]) {
                    minRank = individual.rank[task-1];
                    // bestTask = task;
                }
                //Nếu rank của cả 2 tác vụ bằng nhau, random 1 trong 2
                // else if (minRank == individual.rank[task-1]) {
                //     if (Settings.random.nextDouble() < 0.5f) {
                //         bestTask = task;
                //     }
                // }
            }
            // individual.skill_factor = bestTask;
            individual.scalar_fitness = (1.0/(minRank+1));
        }
    }

    public void Selection() {
        //Sắp xếp quần thể theo scalar fitness giảm dần
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return -Double.valueOf(o1.scalar_fitness).compareTo(o2.scalar_fitness);
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
