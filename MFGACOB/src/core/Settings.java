package core;
import java.util.Random;

public class Settings {
    public static Random random = new Random();

    public static int SIMULATION_TIME = 30; //Số lần chạy thuật toán

    //=============ACO params=============
    public static final int ANT_GENERATION = 5;
    public static final int ANT_POPULATION_SIZE = 20;
    public static final float DULL_ANT_RATE = 0.05f;

    public static final double INIT_PHEROMONE = 0.00001;
    public static final double EVAPORATE_RATE = 0.05;
    public static final double MIN_PHEROMONE = 0.01;
    public static final double MAX_PHEROMONE = 1;

	public static final int ALPHA = 3;
	public static final int BETA = 5;
	public static final int GAMMA = 5;
	public static final double Q = 5;
    public static final double C = 5; //Trọng số thêm vào cạnh

    //=============SA-MFEA params=============
    public static final int MFEA_GENERATION = 20; 
    public static final int MFEA_POPULATION_SIZE = 25;

    public static final int HISTORICAL_MEMORY_SIZE = 30;
    
    //=============REPRODUCTION params=============
    public static final int SBX_nc = 2;
    public static final double SBX_MIN_LIMIT = 0.00001;
    public static final int PM_nm = 2;
}
