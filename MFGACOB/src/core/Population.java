package core;

import java.util.ArrayList;

public class Population {
    ArrayList<Individual> pop;
    int chromosomeLength = -1;

    public Population(Task[] tasks) {
        for(Task t: tasks){
            this.chromosomeLength = Math.max(this.chromosomeLength, t.domainNumber);
        }
        pop = new ArrayList<Individual>();
    }
}
