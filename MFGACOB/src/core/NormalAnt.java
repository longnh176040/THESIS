package core;

import java.util.ArrayList;

import util.selection.RouletteWheel;

public class NormalAnt extends Ant{

    @Override
    public Path FindingEDUPath(Task task, double[] chromosome, Path localBestPath) {
        ArrayList<Edge> candidateEdges = new ArrayList<Edge>(); //List of candidate edges of each move

        ArrayList<Integer> visitedNodes = new ArrayList<Integer>(); //List of visited nodes
        ArrayList<Integer> visitedDomains = new ArrayList<Integer>(); //List of visited domains

        Path path = new Path();

        int d = -1; //The edge’s domain that the ant is visiting;
        int c = 0;  //The cost took from start node to current node;
        int cur = task.sourceNode; //The current visiting node;
        int t = task.targetNode;

        double pe; //transition probability function of edge e
        ArrayList<Double> peList = new ArrayList<Double>(); //list of transition probability function of candidate edges

        while(cur != t) {
            candidateEdges.clear();
            peList.clear();

            //Thêm node đang thăm vào ds node đã thăm
            
            visitedNodes.add(cur - 1);

            //Tìm kiếm tập cạnh ứng cử viên
            for (ArrayList<Edge> outNode : task.nodeList.get(cur - 1).outNodeList) {
                //Bỏ qua những node đã thăm hoặc những node ko có đường đi
                if (outNode.isEmpty() || 
                visitedNodes.contains(task.nodeList.get(cur - 1).outNodeList.indexOf(outNode))) {
                    continue;
                }
                //System.out.println("check visited nodes " + task.nodeList.get(cur - 1).outNodeList.indexOf(outNode));

                for (Edge e : outNode) {
                    //Bỏ qua những cạnh có miền đã thăm
                    if (visitedDomains.contains(e.domain)) continue;
                    else {
                        //Kiểm tra checksum với cạnh e và checkblacklist của node mà cạnh e dẫn đến
                        if (CheckSum(c, e, localBestPath) && 
                        CheckBlacklist(e, visitedDomains, task.nodeList.get(e.outNode - 1).blackList)) {
                            //Nếu thỏa mãn -> Add cạnh này vào tập ứng cử viên cạnh
                            candidateEdges.add(e);
                        }
                    }
                }
            }

            //Nếu không tìm được cạnh ứng cử viên => add chuỗi miền hiện tại vào blacklist của node đang xét
            if (candidateEdges.isEmpty()) {
                if (visitedDomains.size() > 0) {
                    task.nodeList.get(cur-1).AddToBlacklist(visitedDomains);
                }
                //System.out.println("Tập ứng cử viên = rỗng");
                return null;
            }
            else {
                Edge visitingEdge;
                if (candidateEdges.size() == 1) {
                    visitingEdge = candidateEdges.get(0);
                    //System.out.println("Tập ứng cử viên = 1");
                }
                else {
                    for (Edge cEdge : candidateEdges) {
                        pe = Math.pow(cEdge.pheromone, Settings.ALPHA) *
                            Math.pow((1.0/(double) cEdge.weight), Settings.BETA) *
                            Math.pow(chromosome[cEdge.domain-1], Settings.GAMMA);
                        // if (pe == 0) {
                        //     System.out.println("================pe bang 0============= " + 
                        //     Math.pow(cEdge.pheromone, Settings.ALPHA) + " " + 
                        //     Math.pow((1.0/(double) (cEdge.weight + Settings.C)), Settings.BETA) + " " +
                        //     Math.pow(chromosome[cEdge.domain-1], Settings.GAMMA) + " " +
                        //     cEdge.pheromone + " " + 1.0/(double) cEdge.weight + " " + chromosome[cEdge.domain-1]
                        //     );
                        // }
                        peList.add(pe);
                    }
                    //Dùng Roulette chọn 1 cạnh để thăm
                    visitingEdge = RouletteWheel.RouletteSelection(candidateEdges, peList);
                    //System.out.println("Picked an edge " + visitingEdge.inNode + " " + visitingEdge.outNode);
                }
                
                //Thăm cạnh vừa chọn vào đường đi, cập nhật các thông số
                path.path.add(visitingEdge);

                cur = visitingEdge.outNode;
                c += visitingEdge.weight;
                
                //Thêm miền của cạnh đang thăm vào ds miền đã thăm
                if (d != visitingEdge.domain) {
                    if (d != -1) visitedDomains.add(d);
                    d = visitingEdge.domain;
                }
            }
        }

        path.cost = c;
        //Cập nhật giá trị pheromone thêm sau khi đến đích trên các cạnh
        for (int i = 0; i < path.path.size(); i++){
            path.path.get(i).delta += Settings.Q / (double) c;
        }

        // if (c < localBestPath.cost) {
        //     localBestPath = path;
        //     localBestPath.cost = c;
        // }
        return path;
    }

    @Override
    public Path FindingNDUPath(Task task, double[] chromosome, Path localBestPath) {
        ArrayList<Edge> candidateEdges = new ArrayList<Edge>(); //List of candidate nodes of each move

        ArrayList<Integer> visitedNodes = new ArrayList<Integer>(); //List of visited nodes
        ArrayList<Integer> visitedDomains = new ArrayList<Integer>(); //List of visited domains

        Path path = new Path();

        int d = -1; //The edge’s domain that the ant is visiting;
        int c = 0;  //The cost took from start node to current node;
        int cur = task.sourceNode; //The current visiting node;
        int t = task.targetNode;

        double pn; //transition probability function of node n
        ArrayList<Double> pnList = new ArrayList<Double>(); //list of transition probability function of candidate nodes

        while(cur != t) {
            candidateEdges.clear();
            pnList.clear();

            //Thêm node đang thăm vào ds node đã thăm
            visitedNodes.add(cur - 1);

            //Tìm kiếm tập cạnh ứng cử viên
            for (ArrayList<Edge> outNode : task.nodeList.get(cur - 1).outNodeList) {
                //Bỏ qua những node đã thăm hoặc những node ko có đường đi
                if (outNode.isEmpty() ||
                visitedNodes.contains(task.nodeList.get(cur - 1).outNodeList.indexOf(outNode))) {
                    continue;
                }
                //System.out.println("check visited nodes " + task.nodeList.get(cur - 1).outNodeList.indexOf(outNode));

                for (Edge e : outNode) {
                    //Bỏ qua những cạnh dẫn tới node có miền đã thăm
                    if (visitedDomains.contains(task.nodeList.get(e.outNode-1).domain)) continue;
                    else {
                        //Kiểm tra checksum với cạnh e và checkblacklist của node mà cạnh e dẫn đến
                        if (CheckSum(c, e, localBestPath) && 
                        CheckBlacklist(e, visitedDomains, task.nodeList.get(e.outNode - 1).blackList)) {
                            //Nếu thỏa mãn -> Add cạnh này vào tập ứng cử viên cạnh
                            candidateEdges.add(e);
                        }
                    }
                }
            }

            //Nếu không tìm được cạnh ứng cử viên => add chuỗi miền hiện tại vào blacklist của node đang xét
            if (candidateEdges.isEmpty()) {
                if (visitedDomains.size() > 0) {
                    task.nodeList.get(cur-1).AddToBlacklist(visitedDomains);
                }
                //System.out.println("Tập ứng cử viên = rỗng");
                return null;
            }
            else {
                Edge visitingEdge;
                if (candidateEdges.size() == 1) {
                    visitingEdge = candidateEdges.get(0);
                    //System.out.println("Tập ứng cử viên = 1");
                }
                else {
                    for (Edge cEdge : candidateEdges) {
                        pn = Math.pow(cEdge.pheromone, Settings.ALPHA) *
                            Math.pow((1.0/(double) cEdge.weight), Settings.BETA) *
                            Math.pow(chromosome[task.nodeList.get(cEdge.outNode - 1).domain-1], Settings.GAMMA);
                        
                        pnList.add(pn);
                    }
                    //Dùng Roulette chọn 1 cạnh để thăm
                    visitingEdge = RouletteWheel.RouletteSelection(candidateEdges, pnList);
                    //System.out.println("Picked an edge " + visitingEdge.inNode + " " + visitingEdge.outNode);
                }
                
                //Thăm cạnh vừa chọn vào đường đi, cập nhật các thông số
                path.path.add(visitingEdge);

                cur = visitingEdge.outNode;
                c += visitingEdge.weight;
                
                //Thêm miền của cạnh đang thăm vào ds miền đã thăm
                if (d != visitingEdge.domain) {
                    if (d != -1) visitedDomains.add(d);
                    d = visitingEdge.domain;
                }
            }
        }

        path.cost = c;
        //Cập nhật giá trị pheromone thêm sau khi đến đích trên các cạnh
        for (int i = 0; i < path.path.size(); i++){
            path.path.get(i).delta += Settings.Q / (double) c;
        }

        return path;
    }
}
