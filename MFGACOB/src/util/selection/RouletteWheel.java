package util.selection;

import java.util.ArrayList;

import core.Edge;
import core.Settings;

public class RouletteWheel {
    public static Edge RouletteSelection(ArrayList<Edge> candidateEdges, ArrayList<Double> peList) {
        double sumPE = 0;
        double[] pe = new double[peList.size()];

        for (Double d : peList) {
            sumPE += d;
        }

        pe[0] = peList.get(0) / sumPE;
        for (int i = 1; i < pe.length; i++) {
            pe[i] = pe[i-1] + peList.get(i) / sumPE;
        }

        double rand = Settings.random.nextDouble();

        if (rand <= pe[0]) return candidateEdges.get(0);
        else if (rand > pe[pe.length-1]) return candidateEdges.get(candidateEdges.size());
        else {
            for (int i = 1; i < pe.length; i++) {
				if (rand > pe[i - 1] && rand <= pe[i]) {
					return candidateEdges.get(i);
				}
			}
        }
        return null;
    }
}
