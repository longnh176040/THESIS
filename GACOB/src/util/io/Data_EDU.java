package util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import core.Edge;
import core.Node;
import core.Settings;
import core.Task;
import main.Main;

public class Data_EDU {
    public static Task[] ReadFolder(File directory) throws FileNotFoundException {
        File[] listFiles = directory.listFiles();
        Task[] tasks = new Task[listFiles.length];

        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = TaskInit(listFiles[i]);
        }
        return tasks;
    }
    
    public static Task TaskInit(File file) throws FileNotFoundException {
        Main.scanner = new Scanner(file);

        int edgeNumber = 0;
        int nodeNumber = Main.scanner.nextInt();
        int domainNumber = Main.scanner.nextInt();
        int sourceNode = Main.scanner.nextInt();
        int targetNode = Main.scanner.nextInt();

        ArrayList<Node> nodeList = new ArrayList<Node>();
		for (int i = 0; i < nodeNumber; i++) {
			Node node = new Node();
			nodeList.add(node);
		}

        for (int i = 0; i < nodeNumber; i++) {
            for (int j = 0; j < nodeNumber; j++) {
				nodeList.get(i).outNodeList.add(new ArrayList<Edge>());
			}
        }

        int inNode = -1, outNode = -1, weight = 0, domain = -1;
        while (Main.scanner.hasNextLine()) {
            inNode = Main.scanner.nextInt();
			outNode = Main.scanner.nextInt();
			weight = Main.scanner.nextInt();
			domain = Main.scanner.nextInt();

            Edge e = new Edge(inNode, outNode, weight, domain, Settings.INIT_PHEROMONE, 0, false);

            if (nodeList.get(inNode-1).outNodeList.get(outNode-1) == null || 
            nodeList.get(inNode-1).outNodeList.get(outNode-1).isEmpty()) {
                //nodeList.get(inNode-1).outNodeList.add(outNode-1, new ArrayList<Edge>());
                nodeList.get(inNode-1).outNodeList.get(outNode-1).add(e);
                edgeNumber++;
            }
            //Nếu đã add 1 cạnh giữa 2 node trước đó, kiểm tra xem cạnh tiếp theo có trùng miền ko
            else {
                boolean canAddToList = true;
                for (Edge edge : nodeList.get(inNode-1).outNodeList.get(outNode-1)) {
                    if (e.domain == edge.domain) {
                        canAddToList = false;
                        //=> nếu có thì lọc cạnh lớn hơn
                        if (e.weight < edge.weight) {
                            edge.weight = e.weight;
                            break;
                        }
                    }
                }
                //=> nếu ko thì add vào list cạnh
                if (canAddToList) {
                    nodeList.get(inNode-1).outNodeList.get(outNode-1).add(e);
                    edgeNumber++;
                }
            }
        }
        Main.scanner.close();
        Task task = new Task(nodeNumber, domainNumber, edgeNumber, sourceNode, targetNode, nodeList);

        return task;
    }

    public static void PrintTask(Task task) {
        System.out.println("     Node Number: " + task.nodeNumber);
		System.out.println("     Domain Number: " + task.domainNumber);
		System.out.println("     Edge Number: " + task.edgeNumber);
		System.out.println("     BEGIN: " + task.sourceNode + " | END: " + task.targetNode);
		for (int j = 0; j < task.nodeList.size(); j++) {
			System.out.println("          NODE " + (j + 1) + ":");
			for (int k = 0; k < task.nodeList.get(j).outNodeList.size(); k++) {
				System.out.println("          	OUT_NODE " + (k + 1) + ":");
				for (int l = 0; l < task.nodeList.get(j).outNodeList.get(k).size(); l++) {
					System.out.println("          	     Edge " + (l + 1) + ": "
							+ task.nodeList.get(j).outNodeList.get(k).get(l).inNode + "; "
							+ task.nodeList.get(j).outNodeList.get(k).get(l).outNode + "; "
							+ task.nodeList.get(j).outNodeList.get(k).get(l).weight + "; "
							+ task.nodeList.get(j).outNodeList.get(k).get(l).domain + "; ");
				}
			}
		}
    }
}
