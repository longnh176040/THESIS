package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import algorithm.GACOB;
import core.Settings;
import core.Task;
import util.io.Data_EDU;

public class Main {
    public static Scanner scanner;
    
    public static void main(String[] args) throws Exception {
        try {
            File f1 = new File("IDPC-EDU-data\\set1\\idpc_10x5x425.idpc");
            Task t1 = Data_EDU.TaskInit(f1);
            //Data_EDU.PrintTask(t);

            //File directory = new File("data\\IDPC-EDU-data\\test");
            //Task[] tasks = DataIO_EDU.ReadFolder(directory);
            
            // for (Task task : tasks) {
            //     DataIO_EDU.PrintTask(task);
            // }

            GACOB gacob = new GACOB(t1, Settings.GA_POPULATION_SIZE);

            double[] convergenceTrend = new double[Settings.GA_GENERATION];
            for (int i = 0; i < Settings.SIMULATION_TIME; i++) {
                gacob.run(i, convergenceTrend);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("ERROR 404: File not found!");
			e.printStackTrace();
        }
    }
}
