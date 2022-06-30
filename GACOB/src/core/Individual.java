package core;

public class Individual {
    public int chromosomeLength;
    
    public double[] chromosome;
    public double fitness;
    public int cost;

    public Individual(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
        this.chromosome = new double[chromosomeLength];
        this.fitness = Double.MAX_VALUE; 
        this.cost =  Integer.MAX_VALUE;
    }

    public void RandomInit() {
        for(int i = 0; i < chromosome.length; i++) {
            double c = 1e-6 + (1.0 - 1e-6) * Settings.random.nextDouble();
            this.chromosome[i] = c;
            //System.out.print(this.chromosome[i] + " ");
        }
        //System.out.println();
    }

    public double[] ChromosomeFixer(double[] chromosome, int length) {
        //Sửa độ dài cá thể chỉ giữ lại bằng số miền
        double[] newChromosome = new double[length];
        for (int i = 0; i < newChromosome.length; i++) {
            newChromosome[i] = chromosome[i];
        }
        return newChromosome;
    }

    public void EvaluateFitness(Task task) {
        //Đánh giá cá thể trên tác vụ
        ACO tACO = new ACO();
        tACO.UpdatePheromone(task);
        this.cost = tACO.AntFindingEDUPath(task, chromosome);
        //System.out.print(this.cost[i] + " ");
        this.fitness = 1/ (double) this.cost;
        //System.out.print(this.fitness[i] + " ");
        //System.out.println();
    }
}
