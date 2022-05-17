package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import core.Individual;
import core.Population;
import core.Settings;
import core.Task;
import util.reproduction.Crossover;
import util.reproduction.Mutation;

public class SA_MFEA {
    Task[] tasks;
    
    double[][][] historical_memory; //Danh sách bộ nhớ lịch sử
    double[][] success_rmp;         //Danh sách rmp thành công
    int [][] memoryPos;          //Vị trí tiếp theo để cập nhật trong bộ nhớ
    double[][] sum_rmp;
    boolean change[][];

    int popSize;    //Kích thước quần thể khởi tạo
    int subpopSize; //Số lượng cá thể khởi tạo ứng với từng tác vụ 

    int historical_memory_size; //Kích thước của bộ nhớ lịch sử

    Random random;

    int generation = 0; //Biến dùng để đếm số thế hệ

    public SA_MFEA(Task[] tasks, int popSize, int historical_memory_size) {
        this.tasks = tasks;
        this.popSize = popSize;
        this.historical_memory_size = historical_memory_size;

        historical_memory = new double[tasks.length][tasks.length][historical_memory_size];
        success_rmp = new double[tasks.length][tasks.length];
        memoryPos = new int[tasks.length][tasks.length];
        sum_rmp = new double[tasks.length][tasks.length];
        change = new boolean[tasks.length][tasks.length];

        random = Settings.random;
    }

    public ArrayList<Individual> run (int seed, double[][] convergenceTrend) {
        long start =  System.currentTimeMillis();

        //Khởi tạo bộ nhớ lịch sử
        for (int i = 0; i < tasks.length; i++) {
            for (int j = i + 1; j < tasks.length; j++) {
                for (int k = 0; k < historical_memory_size; k++) {
                    historical_memory[i][j][k] = historical_memory[j][i][k] = 0.5;
                }
                memoryPos[i][j] = memoryPos[j][i] = 0;
            }
        }

        random = new Random(seed);
        ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();

        //Khởi tạo quần thể đầu tiên
        Population population = new Population(tasks, popSize);
        population.InitPopulation();

        // for (Individual indiv: population.population) {
        //     if (indiv.skill_factor == 1) {
        //         System.out.println(indiv.fitness[0]);
        //     }
        // }

        population.UpdateRank();
        population.UpdateScalarFitness();
        population.Selection();

        generation = 1;
        for (int i = 1; i <= tasks.length; i++) {
            for (Individual ind : population.population) {
                if (ind.skill_factor == i) 
                {   //Cập nhật các cá thể có fitness lớn nhất
                    convergenceTrend[generation - 1][i - 1] = ind.fitness[i - 1];
                    bestIndividuals.add(ind);
                    break;
                }
            }
        }

        while (generation < Settings.MFEA_GENERATION) {
            generation++;

            System.out.println("===============generation: " + generation + "=================");
            ArrayList<Individual> offspringPopulation = Reproduction(population);
            population.population.addAll(offspringPopulation);
            //System.out.println("pop_size: " + population.population.size());
            population.UpdateRank();
            population.UpdateScalarFitness();
            population.Selection();

            for (int i = 1; i <= tasks.length; i++) {
                for (Individual ind : population.population) {
                    if (ind.skill_factor == i) 
                    {   //Cập nhật các cá thể có fitness lớn nhất
                        if (bestIndividuals.get(i-1).fitness[i-1] > ind.fitness[i-1]) {
                            bestIndividuals.set(i-1, ind);
                        }
                        convergenceTrend[generation-1][i-1] = bestIndividuals.get(i-1).fitness[i-1];
                        break;
                    }
                }
            }

            System.out.println("Best individual " + population.population.get(0).cost[population.population.get(0).skill_factor-1]);
            System.out.println("Best individual " + population.population.get(1).cost[population.population.get(1).skill_factor-1]);

        }
        System.out.println("Seed " + seed);
        for (int i = 1; i <= tasks.length; i++) {
            System.out.println("Skill factor: " + bestIndividuals.get(i - 1).skill_factor + 
            " Fitness: " + bestIndividuals.get(i - 1).fitness[i - 1] +
            " Cost: " + bestIndividuals.get(i - 1).cost[i - 1]
            );

        }
        long end =  System.currentTimeMillis();
        System.out.println(end-start);

        return bestIndividuals;
    }

    public ArrayList<Individual> Reproduction(Population population) {
        //Quần thể con sau quá trình reproduction
        ArrayList<Individual> offspringPop = new ArrayList<Individual>();

        //List sử dụng cho việc shuffle + random + ....
        ArrayList<Integer> randList = new ArrayList<Integer>();

        for (int i = 0; i < tasks.length; i++) {
            for (int j = i + 1; j < tasks.length; j++) {
                sum_rmp[i][j] = sum_rmp[j][i] = 0;
                success_rmp[i][j] = success_rmp[j][i] = 0;
                change[i][j] = change[j][i] = false;
            }
        }

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

            //Lấy ngẫu nhiên 1 phần tử trong bộ nhớ lịch sử 
            double hm_element = historical_memory[parent1.skill_factor - 1][parent2.skill_factor - 1][random.nextInt(historical_memory_size)];
            //Tính rmp giữa 2 tác vụ a và b = phân phối Gauss
            double rmp_ab;
            do {
                rmp_ab = gauss(hm_element, 0.1);
            } while (rmp_ab <= 0);
            if (rmp_ab > 1) rmp_ab = 1;
            
            double improvement_percentage = 0; //Giá trị phần trăm cải thiện

            if (parent1.skill_factor == parent2.skill_factor) {
                //Lai ghép 2 cha mẹ
                offspring = Crossover.SBXCrossover(parent1, parent2);
                for (Individual individual : offspring) {
                    Mutation.PolynomialMutation(individual);
                    individual.skill_factor = parent1.skill_factor;
                }
                population.SwapIndividuals(offspring.get(0), offspring.get(1));
                //Đánh giá các cá thể với tác vụ nó làm tốt nhất
                for (Individual individual : offspring) {
                    individual.EvaluateFitnessBestTask(tasks);
                }
            }
            else if (random.nextDouble() < rmp_ab) {
                offspring = Crossover.SBXCrossover(parent1, parent2);
                for (Individual individual : offspring) {
                    Mutation.PolynomialMutation(individual);

                    //Gán skill factor cho các cá thể con theo cha mẹ với tỉ lệ 50%
                    if (random.nextDouble() < 0.5) {
                        individual.skill_factor = parent1.skill_factor;
                        individual.EvaluateFitnessBestTask(tasks);

                        //Tính % cải thiện
                        improvement_percentage = Math.max(improvement_percentage, 
                        (parent1.fitness[parent1.skill_factor-1] - individual.fitness[individual.skill_factor-1]) / parent1.fitness[parent1.skill_factor-1]);
                    } else {
                        individual.skill_factor = parent2.skill_factor;
                        individual.EvaluateFitnessBestTask(tasks);

                        improvement_percentage = Math.max(improvement_percentage, 
                        (parent2.fitness[parent2.skill_factor-1] - individual.fitness[individual.skill_factor-1]) / parent2.fitness[parent2.skill_factor-1]);
                    }
                }
            }
            else {  //Lai ghép liên tác vụ
                //Tìm kiếm 1 cá thể có skill factor = parent1 
                int x = random.nextInt(population.popSize);
                while (x == a || population.population.get(x).skill_factor != parent1.skill_factor) {
                    x = random.nextInt(population.popSize);
                }
                Individual parent1_1 = population.population.get(x);
                ArrayList<Individual> offspring1 = Crossover.SBXCrossover(parent1, parent1_1);
                for (Individual ind : offspring1) {
                    Mutation.PolynomialMutation(ind);
                }
                population.SwapIndividuals(offspring1.get(0), offspring1.get(1));
                Individual child1 = offspring1.get(0);
                child1.skill_factor = parent1.skill_factor;

                //Tìm kiếm 1 cá thể có skill factor = parent2 
                int y = random.nextInt(population.popSize);
                while (y == b || population.population.get(y).skill_factor != parent2.skill_factor) {
                    y = random.nextInt(population.popSize);
                }
                Individual parent2_1 = population.population.get(y);
                ArrayList<Individual> offspring2 = Crossover.SBXCrossover(parent2, parent2_1);
                for (Individual ind : offspring2) {
                    Mutation.PolynomialMutation(ind);
                }
                population.SwapIndividuals(offspring2.get(0), offspring2.get(1));
                Individual child2 = offspring2.get(0);
                child2.skill_factor = parent2.skill_factor;

                //Chỉ lấy 2 cá thể để add vào quần thể mới
                offspring = new ArrayList<>();
                offspring.add(child1);
                offspring.add(child2);
                for (Individual individual : offspring) {
                    individual.EvaluateFitnessBestTask(tasks);
                }

                improvement_percentage = Math.max(improvement_percentage, 
                (parent2.fitness[parent2.skill_factor-1] - child2.fitness[child2.skill_factor-1]) / parent2.fitness[parent2.skill_factor-1]);
                improvement_percentage = Math.max(improvement_percentage, 
                (parent1.fitness[parent1.skill_factor-1] - child1.fitness[child1.skill_factor-1]) / parent1.fitness[parent1.skill_factor-1]);
            }

            if (improvement_percentage > 0 && parent2.skill_factor != parent1.skill_factor) {
                change[parent2.skill_factor - 1][parent1.skill_factor - 1] = change[parent1.skill_factor - 1][parent2.skill_factor - 1] = true;
                success_rmp[parent2.skill_factor - 1][parent1.skill_factor - 1] += improvement_percentage * rmp_ab * rmp_ab;
                success_rmp[parent1.skill_factor - 1][parent2.skill_factor - 1] = success_rmp[parent2.skill_factor - 1][parent1.skill_factor - 1];

                sum_rmp[parent1.skill_factor - 1][parent2.skill_factor - 1] += improvement_percentage * rmp_ab;
                sum_rmp[parent2.skill_factor - 1][parent1.skill_factor - 1] = sum_rmp[parent1.skill_factor - 1][parent2.skill_factor - 1];
            }

            offspringPop.addAll(offspring);
        }

        //Cập nhật bộ nhớ lịch sử 
        for (int i = 0; i < tasks.length; i++) {
            for (int j = i + 1; j < tasks.length; j++) {
                if (change[i][j]) {
                    historical_memory[i][j][memoryPos[i][j]] = success_rmp[i][j] / sum_rmp[i][j];
                    historical_memory[j][i][memoryPos[j][i]] = historical_memory[i][j][memoryPos[i][j]];
                    memoryPos[i][j] = (memoryPos[i][j] + 1) % historical_memory_size;
                    memoryPos[j][i] = memoryPos[i][j];
                }
            }
        }
        return offspringPop;
    }

    double gauss(double mu, double sigma) {
        return mu + sigma * Math.sqrt(-2.0 * Math.log(random.nextDouble())) * Math.sin(2.0 * Math.PI * random.nextDouble());
    }
}
