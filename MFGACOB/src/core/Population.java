package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {
    ArrayList<Individual> population;
    Task[] tasks;
    int chromosomeLength = -1;
    int taskNum;

    public Population(Task[] tasks) {
        this.taskNum = tasks.length;
        this.tasks = tasks;
        for(Task t: tasks){
            this.chromosomeLength = Math.max(this.chromosomeLength, t.domainNumber);
        }
        this.population = new ArrayList<Individual>();
    }

    public void InitPopulation() {
        for (int i = 0; i < Settings.MFEA_POPULATION_SIZE; i++) {
            Individual individual = new Individual(taskNum, chromosomeLength);
            individual.RandomInit();
            individual.EvaluateFitness(tasks);
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
                    if (o1.fitness[t-1] < o2.fitness[t-1]) return -1;
                    else if (o1.fitness[t-1] > o2.fitness[t-1]) return 1;
                    return 0;
                }
            });
            //Cập nhật ranking của các cá thể ứng với tác vụ đang xét
            for(int i = 0; i < population.size(); i++) {
                population.get(i).rank[task-1] = i;
            }
        }
    }

    public void UpdateScalarFitness() {
        for (Individual individual : population) {
            int minRank = Integer.MAX_VALUE;
            int bestTask = 0;
            //Tìm kiếm tác vụ có rank cao nhất
            for (int task = 1; task <= tasks.length; task++) {
                if (minRank > individual.rank[task-1]) {
                    minRank = individual.rank[task-1];
                    bestTask = task;
                }
                //Nếu rank của cả 2 tác vụ bằng nhau, random 1 trong 2
                else if (minRank == individual.rank[task-1]) {
                    if (Settings.random.nextDouble() < 0.5f) {
                        bestTask = task;
                    }
                }
            }
            individual.skill_factor = bestTask;
            individual.scalar_fitness = (1.0/(minRank+1));
        }
    }

    public void Selection() {
        //Sắp xếp quần thể theo scalar fitness giảm dần
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if(o1.scalar_fitness < o2.scalar_fitness) return -1;
                else if(o1.scalar_fitness > o2.scalar_fitness) return 1;
                else return 0;
            }
        });

        int size = population.size();

        //Giữ lại số cá thể = kích thước quần thể
        if(population.size()> Settings.MFEA_POPULATION_SIZE) 
            population.subList(Settings.MFEA_POPULATION_SIZE, size).clear();  
    }
}
