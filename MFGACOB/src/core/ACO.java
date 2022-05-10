package core;

public class ACO {

    public int AntFindingPath(Task task, double[] chromosome) {
        Path localBestPath = new Path();

        int genACO = Settings.ANT_GENERATION;
        int numAnt = Settings.ANT_POPULATION_SIZE;
        int numDullAnt = (int) (Settings.ANT_POPULATION_SIZE * Settings.DULL_ANT_RATE);
        int numNormalAnt = Settings.ANT_POPULATION_SIZE - numDullAnt;

        double rand;
        Path curPath;

        while (genACO > 0) {
            while (numAnt > 0) {
                rand = Settings.random.nextDouble();

                //Ngẫu nhiên sử dụng kiến thường hoặc kiến điếc mùi
                if ((rand < Settings.DULL_ANT_RATE || numNormalAnt <= 0) && numDullAnt > 0)
                {
                    DullAnt dullAnt = new DullAnt();
                    curPath = dullAnt.FindingEDUPath(task, chromosome, localBestPath);
    
                    numDullAnt--;
                }
                else {
                    NormalAnt normalAnt = new NormalAnt();
                    curPath = normalAnt.FindingEDUPath(task, chromosome, localBestPath);
                    numNormalAnt--;
                }

                //Nếu đường đi tìm thấy tốt hơn localBest thì cập nhật 
                if (curPath.cost < localBestPath.cost) {
                    localBestPath.CopyPath(curPath);
                }
                numAnt--;
            }
    
            UpdatePheromone(task);
            genACO--;
        }

        return localBestPath.cost;
    } 

    /*
        Cập nhật pheromone trên tất cả các cạnh
    */
    public void UpdatePheromone(Task task) {
        for (int i = 0; i < task.nodeList.size(); i++) {
			for (int j = 0; j < task.nodeList.get(i).outNodeList.size(); j++) {
				for (Edge edge : task.nodeList.get(i).outNodeList.get(j)) {
					//edge.pheromone = (1 - Settings.EVAPORATE_RATE) * edge.pheromone + Math.log(1 + edge.delta);
                    edge.pheromone = (1 - Settings.EVAPORATE_RATE) * edge.pheromone + edge.delta;
                    edge.delta = 0;

                    if(edge.pheromone < Settings.MIN_PHEROMONE) edge.pheromone = Settings.MIN_PHEROMONE;
                    else if (edge.pheromone > Settings.MAX_PHEROMONE) edge.pheromone = Settings.MAX_PHEROMONE;
				}
			}
		}
    }

    /*
        Khởi tạo pheromone trên tất cả các cạnh
    */
    public void InitPheromone(Task task, double pheromone, double delta) {
        for (int i = 0; i < task.nodeList.size(); i++) {
			for (int j = 0; j < task.nodeList.get(i).outNodeList.size(); j++) {
				for (Edge edge : task.nodeList.get(i).outNodeList.get(j)) {
                    edge.pheromone = pheromone;
                    edge.delta = delta;
				}
			}
		}
    }
}
