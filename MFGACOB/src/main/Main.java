package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import algorithm.SA_MFEA;
import core.Settings;
import core.Task;
import util.io.DataIO_EDU;

public class Main {
    public static Scanner scanner;

    public static void main(String[] args) throws Exception {
        try {
            File directory = new File("data\\IDPC-EDU-data\\set1");
            Task[] tasks = DataIO_EDU.ReadFolder(directory);

            // for (Task task : tasks) {
            //     DataIO_EDU.PrintTask(task);
            // }
            Task[] testTasks = new Task[2];
            testTasks[0] = tasks[0];
            testTasks[1] = tasks[1];

            SA_MFEA sa_MFEA = new SA_MFEA(testTasks, Settings.MFEA_POPULATION_SIZE, Settings.HISTORICAL_MEMORY_SIZE);

            double sum[] = new double[testTasks.length];
            double[][][] convergenceTrend = new double[Settings.SIMULATION_TIME][Settings.MFEA_GENERATION][testTasks.length];
            for (int i = 0; i < Settings.SIMULATION_TIME; i++) {
                sa_MFEA.run(i, convergenceTrend[i]);
                for (int k = 0; k < testTasks.length; k++) {
                    sum[k] += convergenceTrend[i][Settings.MFEA_GENERATION-1][k] / Settings.SIMULATION_TIME;
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("ERROR 404: File not found!");
			e.printStackTrace();
        }
    }
}
