package core;

import java.util.ArrayList;

public class Node {
    public int domain; //used for IDPC-NDU only

    //List of adjacent nodes and their corresponding edges
    public ArrayList<ArrayList<Edge>> outNodeList = new ArrayList<ArrayList<Edge>>();

    //Black-list nodes
    public ArrayList<ArrayList<Integer>> blackList = new ArrayList<ArrayList<Integer>>();

    public Node() { }

    public Node(int domain) {
		this.domain = domain;
    }

    public void AddToBlacklist(ArrayList<Integer> domainString) {
        //TODO: Sort the domain string before saving to the blacklist

        blackList.add(domainString);
    }

    public void ResetBlackList() {
        blackList.clear();
    }
}
