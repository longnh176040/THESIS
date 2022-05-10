package core;

public class Individual {
    public int taskNum, chromosomeLength;
    
    public double[] chromosome;
    public double[] fitness;
    public int[] cost;

    public int skill_factor;
    public double scalar_fitness;
    public int rank[];


    public Individual(int taskNum, int chromosomeLength) {
        this.taskNum = taskNum;
        this.chromosomeLength = chromosomeLength;

        this.chromosome = new double[chromosomeLength];
        this.rank = new int[taskNum];

        this.fitness =  new double[taskNum];
        for(int i = 0; i < taskNum; i++) 
            fitness[i] = Double.MAX_VALUE; 

        this.cost =  new int[taskNum];
        for (int i : this.cost) {
            i = Integer.MAX_VALUE;
        }
    }

    public void RandomInit() {
        for(int i = 0; i < chromosome.length; i++) {
            this.chromosome[i] = Settings.random.nextDouble();
        }
    }

    public double[] ChromosomeFixer(double[] chromosome, int length) {
        //Sửa độ dài cá thể chỉ giữ lại bằng 
        double[] newChromosome = new double[length];
        for (int i = 0; i < newChromosome.length; i++) {
            newChromosome[i] = chromosome[i];
        }
        return newChromosome;
    }

    public void EvaluateFitnessAllTasks(Task[] tasks) {
        //Đánh giá cá thể với từng tác vụ
        for (int i = 0; i < tasks.length; i++) {
            double[] tChromosome;
            if (chromosomeLength > tasks[i].domainNumber) {
                tChromosome = ChromosomeFixer(chromosome, tasks[i].domainNumber);
            }
            else {
                tChromosome = this.chromosome;
            }

            ACO tACO = new ACO();
            tACO.UpdatePheromone(tasks[i]);
            this.cost[i] = tACO.AntFindingPath(tasks[i], tChromosome);
            this.fitness[i] = 1/ (double) this.cost[i];
        }
    }

    public void EvaluateFitnessBestTask(Task[] tasks) {
        //Đánh giá cá thể với từng tác vụ
        for (int i = 0; i < tasks.length; i++) {
            if (i == skill_factor) {
                double[] tChromosome;
                if (chromosomeLength > tasks[i].domainNumber) {
                    tChromosome = ChromosomeFixer(chromosome, tasks[i].domainNumber);
                }
                else {
                    tChromosome = this.chromosome;
                }

                ACO tACO = new ACO();
                tACO.UpdatePheromone(tasks[i]);
                this.cost[i] = tACO.AntFindingPath(tasks[i], tChromosome);
            }
            else {
                this.cost[i] = Integer.MAX_VALUE;
            }
            this.fitness[i] = 1/ (double) this.cost[i];
        }
    }
}
