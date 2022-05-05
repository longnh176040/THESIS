package core;

import java.util.ArrayList;
import java.util.Collections;

public class Node {
    public int domain; //used for IDPC-NDU only

    //List of adjacent nodes and their corresponding edges
    public ArrayList<ArrayList<Edge>> outNodeList = new ArrayList<ArrayList<Edge>>();

    //Black-list nodes
    public ArrayList<ArrayList<Integer>> blackList = new ArrayList<ArrayList<Integer>>();

    public Node() { }

    public Node(int domain) { //used for IDPC-NDU only
		this.domain = domain;
    }

    public void AddToBlacklist(ArrayList<Integer> domainString) {
        //Sort the domain string before saving to the blacklist except the last domain
        int lastElement = domainString.get(domainString.size()-1);
        domainString.remove(domainString.size()-1);
        
        Collections.sort(domainString);
        domainString.add(lastElement);

        blackList.add(domainString);
    }

    public void ResetBlackList() {
        blackList.clear();
    }
}
