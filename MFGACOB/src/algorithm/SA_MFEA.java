package algorithm;

import java.util.ArrayList;
import java.util.Collections;

import core.Individual;
import core.Population;
import core.Settings;
import core.Task;

public class SA_MFEA {
    Task[] tasks;
    
    double[][][] historical_memory; //Danh sách bộ nhớ lịch sử
    double[][] success_rmp;         //Danh sách rmp thành công
    double [][] memoryPos;          //Vị trí tiếp theo để cập nhật trong bộ nhớ
    double[][] sum_rmp;
    boolean change[][];

    int popSize;    //Kích thước quần thể khởi tạo
    int subpopSize; //Số lượng cá thể khởi tạo ứng với từng tác vụ 

    int historical_memory_size; //Kích thước của bộ nhớ lịch sử

    public SA_MFEA(Task[] tasks, int popSize, int historical_memory_size) {
        this.tasks = tasks;
        this.popSize = popSize;
        this.historical_memory_size = historical_memory_size;

        historical_memory = new double[tasks.length][tasks.length][historical_memory_size];
        success_rmp = new double[tasks.length][tasks.length];
        memoryPos = new double[tasks.length][tasks.length];
        sum_rmp = new double[tasks.length][tasks.length];
        change = new boolean[tasks.length][tasks.length];
    }


    public ArrayList<Individual> Reproduction(Population population) {
        //Quần thể con sau quá trình reproduction
        ArrayList<Individual> offspring = new ArrayList<Individual>();

        //List sử dụng cho việc shuffle + random + ....
        ArrayList<Integer> randList = new ArrayList<Integer>();

        for (int i = 0; i < tasks.length; i++) {
            for (int j = i + 1; j < tasks.length; j++) {
                sum_rmp[i][j] = sum_rmp[j][i] = 0;
                success_rmp[i][j] = success_rmp[j][i] = 0;
                change[i][j] = change[j][i] = false;
            }
        }

        for (int i = 0; i < population.population.size(); i++) {
            randList.add(i);
        }
        Collections.shuffle(randList, Settings.random);

        

        return offspring;
    }
}
