package core;

import java.util.ArrayList;

public class NormalAnt extends Ant{

    @Override
    public Path FindingEDUPath(Task task, double[] gene) {
        ArrayList<Edge> candidateEdges = new ArrayList<Edge>(); //List of candidate edges of each move

        ArrayList<Integer> visitedNodes = new ArrayList<Integer>(); //List of visited nodes
        ArrayList<Integer> visitedDomains = new ArrayList<Integer>(); //List of visited domains

        Path path = new Path();

        int d = -1; //The edge’s domain that the ant is visiting;
        int c = 0;  //The cost took from start node to current node;
        int cur = task.sourceNode; //The current visiting node;
        int t = task.targetNode;

        while(cur != t) {
            candidateEdges.clear();

            visitedDomains.add(cur);
            
        }

        return path;
    }
}
