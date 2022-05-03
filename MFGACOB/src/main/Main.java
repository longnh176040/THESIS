package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import core.Task;
import util.io.DataIO_EDU;

public class Main {
    public static Scanner scanner;

    public static void main(String[] args) throws Exception {
        try {
            File file = new File("data\\IDPC-EDU-data\\set1\\idpc_10x5x425.idpc");
            Task task = DataIO_EDU.TaskInit(file);
            DataIO_EDU.PrintTask(task);
        }
        catch (FileNotFoundException e){
            System.out.println("ERROR 404: File not found!");
			e.printStackTrace();
        }

    }
}
