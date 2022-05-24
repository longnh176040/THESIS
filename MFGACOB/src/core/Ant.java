package core;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Ant {
    public abstract Path FindingEDUPath(Task task, double[] gene, Path localBestPath);

    public abstract Path FindingNDUPath(Task task, double[] gene, Path localBestPath);

    /*
    true nếu cạnh đang xét không vượt quá localBestPath
    */
    public boolean CheckSum(float currentCost, Edge edge, Path localBestPath) {
        if (currentCost + edge.weight < localBestPath.cost) {
            return true;
        } else return false;
    }

    /*
    true nếu chuỗi miền đang thăm không nằm trong blacklist
    */
    public boolean CheckBlacklist(Edge edge, ArrayList<Integer> visitedDomains, ArrayList<ArrayList<Integer>> blackList) {
        ArrayList<ArrayList<Integer>> stuckList = new ArrayList<ArrayList<Integer>>();

        for (ArrayList<Integer> ai: blackList) {
            //Kiểm tra xem miền của cạnh e có phải là miền gây ra tắc của bất kỳ chuỗi nào trong blacklist ko
            if (edge.domain == ai.get(ai.size()-1)) {
                //Thêm vào stucklist chuỗi miền phía trước miền gây ra tắc
                ArrayList<Integer> tmpList = new ArrayList<Integer>();
                for(int i = 0; i < ai.size()-1; i++) {
                    tmpList.add(ai.get(i));
                }
                stuckList.add(tmpList);
            }
        }
        //Kiếm tra xem chuỗi miền đang thăm hiện tại có là tập con của chuỗi bất kỳ trong stucklist
        for (ArrayList<Integer> ai : stuckList) {
            if (ai.containsAll(visitedDomains)) {
                return false;
            }
        }
        return true;
    }
}
