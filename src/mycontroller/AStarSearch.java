package mycontroller;

import java.util.*;

import javax.swing.text.Utilities;

import world.Car;
import world.World;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;
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
				//System.out.println(cameFrom);
				return new Pair<Stack<Coordinate>, Float>(reconstructPath(curr), gCosts.get(goal));
			}
			
			unExplored.remove(curr);
			exploredTiles.add(curr);
			
			
			
			ArrayList<Coordinate> neighbours = getValidNeighbours(curr);
			//System.out.println(neighbours);

			for (Coordinate neighbour: neighbours) {
				if (exploredTiles.contains(neighbour)) {
					continue;
				}
				
				if (!unExplored.containsKey(neighbour)) {
					unExplored.put(neighbour, Float.MAX_VALUE);
				}
				//this is null
				//System.out.println(cameFrom.get(curr));
				gCost = gCosts.get(curr) + calcGCosts(curr, neighbour, cameFrom.get(curr));
				//System.out.println(x);
				if (gCosts.containsKey(neighbour)) {
					if (gCost >= gCosts.get(neighbour)) {
						continue;
					}
				}
				
				cameFrom.put(neighbour, curr);
				gCosts.put(neighbour, gCost);
				float hcost = calcHCost(neighbour, goal);
				//System.out.println(hcost);
				fCost = gCost + hcost;
				
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
		//System.out.println(current);
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

        //Collections.reverse(path);
        path.pop();
        return path;
	}
	
	
	
	private static float getManhattanDistance(Coordinate from, Coordinate to) {
		return (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y));
	}
	
	
//	public static Direction getRelativeDirection(Coordinate from, Coordinate to) {
//        // They must be either vertical or horizontal from one another.
//        assert (Utilities.XOR(from.x == to.x, from.y == to.y));
//
//        final int xDisplacement = to.x - from.x;
//        final int yDisplacement = to.y - from.y;
//
//        if (xDisplacement > 0) {
//            return Direction.EAST;
//        } else if (xDisplacement < 0) {
//            return Direction.WEST;
//        } else if (yDisplacement > 0) {
//            return Direction.NORTH;
//        } else if (yDisplacement < 0) {
//            return Direction.SOUTH;
//        }
//
//        // This shouldn't happen, due to the assert statement at the beginning.
//        return null;
//    }

}
	


