package mycontroller;

import java.util.*;
import world.Car;
import world.World;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import tiles.MapTile;
import tiles.TrapTile;

public class AStarSearch {
	
	private static HashMap<Coordinate, MapTile> map;
	private static Coordinate start;
	private static Coordinate goal;
	private static Coordinate beforeStart;
	
	private static MapExpert mapExpert;
	
	private static ArrayList<Coordinate> exploredTiles;
	private static HashMap<Coordinate, Float> unExplored;
	
	private static HashMap<Coordinate, Coordinate> cameFrom;
	private static HashMap<Coordinate, Float> gCosts;
	
	private static HashMap<Coordinate, MapTile.Type> neighbourTiles;
	
	HashMap<Coordinate, MapTile.Type> worldMap = new HashMap<>();
	worldMap = mapExpert.getWorldMap();
	
	public static ArrayList<Coordinate> findBestPath(HashMap<Coordinate, MapTile> iMap, Coordinate iBeforeStart,
            Coordinate iStart, Coordinate iGoal) {
		
		map = iMap;
		start = iStart;
		goal = iGoal;
		beforeStart = iBeforeStart;
		
		exploredTiles = new ArrayList<>();
		unExplored = new HashMap<>();
		cameFrom = new HashMap<>();
		gCosts = new HashMap<>();
		
		gCosts.put(start, 0.0f);
		unExplored.put(start, 0.0f);
		
		Coordinate curr;
		float gCost, fCost;
		
		while (unExplored.size() > 0) {
			curr = getLowestFCost();
			
			if (curr.equals(goal)) {
				return reconstructPath(curr);
			}
			
			unExplored.remove(curr);
			exploredTiles.add(curr);
			
			
			
			ArrayList<Coordinate> neighbours = (ArrayList<Coordinate>) neighbourTiles.keySet();
			
			for (Coordinate neighbour: neighbours) {
				if (exploredTiles.contains(neighbour)) {
					continue;
				}
				
				if (!unExplored.containsKey(neighbour)) {
					unExplored.put(neighbour, Float.MAX_VALUE);
				}
				
				gCost = gCosts.get(neighbour) + calcGCosts(curr, neighbour, cameFrom.get(curr));
				
				if (gCosts.containsKey(neighbour)) {
					if (gCost >= gCosts.get(neighbour)) {
						continue;
					}
				}
				
				cameFrom.put(neighbour, curr);
				gCosts.put(neighbour, gCost);
				fCost = gCost + calcHCost(neighbour, goal);
				unExplored.put(neighbour, fCost);
			}
		
		}
		
		
		return null;
	}
	
	private static Coordinate getLowestFCost() {
		return Collections.min(unExplored.entrySet(), Map.Entry.comparingByValue()).getKey();
	}
	
	private static float calcGCosts(Coordinate current, Coordinate neighbour, Coordinate cameFrom) {
		float gCost = getManhattanDistance(current, neighbour);
		
		//add trap multipliers if needed
		if (worldMap.get(neighbour) )) {
			
		}
		
		return gCost;
	}
	
	private static ArrayList<Coordinate> getValidNeighbours(Coordinate current) {
		neighbourTiles = new HashMap<>();
		ArrayList<Coordinate> validNeighbours = new ArrayList<>();
		neighbourTiles = mapExpert.getNeighbours(current);
		
		for (Coordinate neighbour: neighbourTiles.keySet()) {
			
		}
		
		return validNeighbours;
	}
	
	private static float calcHCost(Coordinate neighbour, Coordinate goal) {
		float hCost = getManhattanDistance(neighbour, goal);
		return hCost;
	}
	
	private static ArrayList<Coordinate> reconstructPath(Coordinate goal) {
		ArrayList<Coordinate> path = new ArrayList<>();
        path.add(goal);
        Coordinate current = goal;
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
	}
	
	private static float getManhattanDistance(Coordinate from, Coordinate to) {
		return (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y));
	}
}
	


