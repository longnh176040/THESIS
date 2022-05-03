package core;

public class Individual {
    public double[] chromosome;
    public double fitness;

    public int cost;

    public Individual(int chromosomeLength) {
        this.chromosome = new double[chromosomeLength];
        this.fitness = 0;
        this.cost = Integer.MAX_VALUE;
    }

    public void RandomInit() {
        for(int i = 0; i<chromosome.length; i++) {
            this.chromosome[i] = Settings.random.nextDouble();
        }
    }
}
