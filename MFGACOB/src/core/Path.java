package core;

import java.util.ArrayList;

public class Path {
    public int cost;
    public ArrayList<Edge> path;

    public Path() {
        this.cost = Integer.MAX_VALUE;
        path = new ArrayList<Edge>();
    }
}
