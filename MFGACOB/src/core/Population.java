package core;

import java.util.ArrayList;

public class Population {
    ArrayList<Individual> pop;
    Task[] tasks;
    int chromosomeLength = -1;
    int taskNum;

    public Population(Task[] tasks) {
        this.taskNum = tasks.length;
        this.tasks = tasks;
        for(Task t: tasks){
            this.chromosomeLength = Math.max(this.chromosomeLength, t.domainNumber);
        }
        this.pop = new ArrayList<Individual>();
    }

    public void InitPopulation() {
        for (int i = 0; i < Settings.MFEA_POPULATION_SIZE; i++) {
            Individual individual = new Individual(taskNum, chromosomeLength);
            individual.RandomInit();
            individual.EvaluateFitness(tasks);
        }
    }
}
