package mycontroller;

import java.util.*;
import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class Astaralt {
		public Stack<Coordinate> aStarSearch(Coordinate start,
	            Coordinate goal)
	{
	
//		Coordinate startNode = pointNodeMap.get(start);
//		Coordinate endNode = pointNodeMap.get(goal);
		
		// setup for A*
		HashMap<Coordinate,MapTile> parentMap = new HashMap<Coordinate,MapTile>();
		HashSet<Coordinate> visited = new HashSet<Coordinate>();
		Map<Coordinate, Double> distances = initializeAllToInfinity();
		
		Queue<Coordinate> priorityQueue = initQueue();
		
		//  enque StartNode, with distance 0
		startNode.setDistanceToStart(new Double(0));
		distances.put(startNode, new Double(0));
		priorityQueue.add(startNode);
		Coordinate current = null;
	
		while (!priorityQueue.isEmpty()) 
		{
			current = priorityQueue.remove();
			
			if (!visited.contains(current) ){
				visited.add(current);
				// if last element in PQ reached
				if (current.equals(endNode)) return reconstructPath(parentMap, startNode, endNode, 0);
				
				Set<Coordinate> neighbors = getNeighbors(current);
				for (Coordinate neighbor : neighbors) {
					if (!visited.contains(neighbor) ){  
					
						// calculate predicted distance to the end node
						double predictedDistance = neighbor.getLocation().distance(endNode.getLocation());
						
						// 1. calculate distance to neighbor. 2. calculate dist from start node
						double neighborDistance = current.calculateDistance(neighbor);
						double totalDistance = current.getDistanceToStart() + neighborDistance + predictedDistance;
						
						// check if distance smaller
						if(totalDistance < distances.get(neighbor) ){
							// update n's distance
							distances.put(neighbor, totalDistance);
							// used for PriorityQueue
							neighbor.setDistanceToStart(totalDistance);
							neighbor.setPredictedDistance(predictedDistance);
							// set parent
							parentMap.put(neighbor, current);
							// enqueue
							priorityQueue.add(neighbor);
						}
					}
				}
			}
		}
	return null;
	}
		
		
	private HashMap<Coordinate, Double> initializeAllToInfinity() {
        HashMap<Coordinate, Double> distances = new HashMap<>();
 
        Iterator<Coordinate> iter = pointNodeMap.values().iterator();
        while (iter.hasNext()) {
            Coordinate node = iter.next();
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        return distances;
    }
}
