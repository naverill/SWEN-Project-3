package mycontroller;

import java.util.*;
import world.Car;
import world.World;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import tiles.MapTile;
import tiles.TrapTile;
import tiles.MudTrap;
import tiles.LavaTrap;
import tiles.GrassTrap;
import tiles.HealthTrap;


public class AStarSearch {
	
	private static HashMap<Coordinate, MapTile> map;
	private static Coordinate start;
	private static Coordinate goal;
	private static Coordinate beforeStart;
	
	private static WorldSensor mapExpert;
	
	private static ArrayList<Coordinate> exploredTiles = new ArrayList<>();
	private static HashMap<Coordinate, Float> unExplored = new HashMap<>();
	
	private static HashMap<Coordinate, Coordinate> cameFrom = new HashMap<>();
	private static HashMap<Coordinate, Float> gCosts = new HashMap<>();
	
	private static HashMap<Coordinate, MapTile> neighbourTiles;
	private static HashMap<Coordinate, MapTile> worldMap = new HashMap<>();
	
	private static final float LAVA_MULTIPLIER = 100.0f;
	private static final float ICE_MULTIPLIER = 0.5f;
	
	public AStarSearch(WorldSensor mapExpert) {
		//this.start = start;
		//this.goal = goal;
		this.mapExpert = mapExpert;
		worldMap = this.mapExpert.getWorldMap();
	}
	 
	
	
	public static Pair<Stack<Coordinate>, Float> findBestPath(HashMap<Coordinate, MapTile> iMap, Coordinate iBeforeStart,
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
				return new Pair<Stack<Coordinate>, Float>(reconstructPath(curr), gCosts.get(goal));
			}
			
			unExplored.remove(curr);
			exploredTiles.add(curr);
			
			
			
			ArrayList<Coordinate> neighbours = getValidNeighbours(curr);
			
			for (Coordinate neighbour: neighbours) {
				if (exploredTiles.contains(neighbour)) {
					continue;
				}
				
				if (!unExplored.containsKey(neighbour)) {
					unExplored.put(neighbour, Float.MAX_VALUE);
				}
				//this is null

				gCost = gCosts.get(curr) + calcGCosts(curr, neighbour, cameFrom.get(curr));
				
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
		MapTile tile = worldMap.get(neighbour);
		if (tile instanceof LavaTrap) {
//			gCost *= (100 - mapExpert.getCar().getHealth());
			gCost *= LAVA_MULTIPLIER;
		}if(tile instanceof HealthTrap) {
			gCost*= ICE_MULTIPLIER;
		}
		return gCost;
	}
	
	
	
	private static ArrayList<Coordinate> getValidNeighbours(Coordinate current) {
		neighbourTiles = new HashMap<>();
		ArrayList<Coordinate> validNeighbours = new ArrayList<>();
		System.out.println(current);
		neighbourTiles = mapExpert.getNeighbours(current);
		
		for (Coordinate neighbour: neighbourTiles.keySet()) {
			
			if (!worldMap.containsKey(neighbour)) {
				continue;
			}
				
			MapTile tile = worldMap.get(neighbour);
			if (tile.isType(MapTile.Type.WALL) || tile.isType(MapTile.Type.EMPTY) || tile instanceof MudTrap) {
				continue;
			}
			
			validNeighbours.add(neighbour);
			
		}
		
		return validNeighbours;
	}
	
	
	
	private static float calcHCost(Coordinate neighbour, Coordinate goal) {
		float hCost = getManhattanDistance(neighbour, goal);
		return hCost;
	}
	
	
	
	private static Stack<Coordinate> reconstructPath(Coordinate goal) {
		Stack<Coordinate> path = new Stack<>();
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
	


