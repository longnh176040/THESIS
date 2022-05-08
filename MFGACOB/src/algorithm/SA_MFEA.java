package algorithm;

import core.Task;

public class SA_MFEA {
    Task[] tasks;
    
    double[][][] historical_memory; //Danh sách bộ nhớ lịch sử
    double[][] success_rmp;         //Danh sách rmp thành công
    double[][] sum_rmp;
    boolean change[][];

    int subpopSize; //Số lượng cá thể khởi tạo ứng với từng tác vụ 
}
