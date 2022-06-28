package core;

public class Edge {
    public int inNode;
    public int outNode;
    public int weight;
    
    public int domain; //used for IDPC-EDU only

    public double pheromone;
    public double delta;

    public boolean isVisited;

    public Edge(int inNode, int outNode, int weight, int domain, double pheromone, double delta, boolean isVisited) {
        this.inNode = inNode;
		this.outNode = outNode;
		this.weight = weight;
		this.domain = domain;
		this.pheromone = pheromone;
		this.delta = delta;
		this.isVisited = isVisited;
    }
}
