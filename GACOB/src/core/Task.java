package core;

import java.util.ArrayList;

public class Task {
    public int nodeNumber;
    public int domainNumber;
    public int edgeNumber;

    public int sourceNode;
    public int targetNode;

    public ArrayList<Node> nodeList = new ArrayList<Node>();

    public Task (int nodeNumber, int domainNumber, int edgeNumber, int sourceNode, int targetNode, ArrayList<Node> nodeList) {
        this.nodeNumber = nodeNumber;
        this.domainNumber = domainNumber;
        this.edgeNumber = edgeNumber;

        this.sourceNode = sourceNode;
        this.targetNode = targetNode;

        this.nodeList = nodeList;
    }

    public void ResetAllBlacklist() {
        for (Node node : nodeList) {
            node.ResetBlackList();
        }
    }
}
