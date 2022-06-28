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
            //System.out.print(pe[i] + " ");
        }
        //System.out.println();

        double rand = Settings.random.nextDouble();

        if (rand <= pe[0]) {
            //System.out.println("Chọn đc cạnh này 1");
            return candidateEdges.get(0);
        }
        else if (rand > pe[pe.length-1]) {
            //System.out.println("Chọn đc cạnh này 2");
            return candidateEdges.get(candidateEdges.size()-1);
        }
        else {
            for (int i = 1; i < pe.length; i++) {
				if (rand > pe[i-1] && rand <= pe[i]) {
                    //System.out.println("Chọn đc cạnh này 3");
					return candidateEdges.get(i);
				}
			}
        }
        //System.out.println("Không chọn đc cạnh nào " + rand);
        // System.out.println("peList size " + peList.size() + "pe length " + pe.length);
        // for (int i = 0; i < peList.size(); i++) {
        //     System.out.print(peList.get(i) + " ");
        // }
        // System.out.println();
        // for (int i = 0; i < pe.length; i++) {
        //     System.out.print(pe[i] + " ");
        // }
        // System.out.println();
        return null;
    }
}
