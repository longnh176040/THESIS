package core;

import java.util.ArrayList;

public class Path {
    public int cost;
    public ArrayList<Edge> path;

    public Path() {
        this.cost = Integer.MAX_VALUE;
        path = new ArrayList<Edge>();
    }

    public void CopyPath(Path path) {
        this.cost = path.cost;
        this.path.clear();
        for (int i = 0; i < path.path.size(); i++) {
            this.path.add(path.path.get(i));
        }
    }
}
