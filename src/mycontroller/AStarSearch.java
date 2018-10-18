package mycontroller;

import java.util.*;

import javax.swing.text.Utilities;

import mycontroller.util.Pair;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;
import tiles.MudTrap;
import tiles.LavaTrap;
import tiles.GrassTrap;
import tiles.HealthTrap;


public class AStarSearch {
	
	private static HashMap<Coordinate, MapTile> map;
	private static Coordinate start;
	private static Coordinate goal;
	private static Coordinate beforeStart;
		
	private static ArrayList<Coordinate> exploredTiles = new ArrayList<>();
	private static HashMap<Coordinate, Float> unExplored = new HashMap<>();
	
	private static HashMap<Coordinate, Coordinate> cameFrom = new HashMap<>();
	private static HashMap<Coordinate, Float> gCosts = new HashMap<>();
	
	private static HashMap<Coordinate, MapTile> neighbourTiles;
	private static HashMap<Coordinate, MapTile> worldMap = new HashMap<>();
	
	private static final float LAVA_MULTIPLIER = 200.0f;
	private static final float ICE_MULTIPLIER = 0.5f;
	private static final float OTW_MULTIPLIER = 0.5f;
	private static final float TURN_MULTIPLIER = 10.0f;

	
	public AStarSearch() {
		worldMap = WorldSensor.getWorldMap();
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
				
				gCost = gCosts.get(curr) + calcGCosts(curr, neighbour, cameFrom.get(curr));
				
				if (gCosts.containsKey(neighbour)) {
					if (gCost >= gCosts.get(neighbour)) {
						continue;
					}
				}
				
				cameFrom.put(neighbour, curr);
				gCosts.put(neighbour, gCost);
				float hcost = calcHCost(neighbour, goal);
				
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
		//TODO GRASS TILE MULTIPLIER
		//add trap multipliers if needed
		MapTile tile = worldMap.get(neighbour);
		if (tile instanceof LavaTrap) {
			if(((LavaTrap) tile).getKey()!=0) {
				if(!WorldSensor.car.getKeys().contains(((LavaTrap) tile).getKey()))
					//doesnt have key then go through it
					gCost *= OTW_MULTIPLIER;
				else {
					//does have key then go through it
					gCost *= LAVA_MULTIPLIER;
				}
			}else {
				gCost *= LAVA_MULTIPLIER;		
			}
		}if(tile instanceof HealthTrap) {
			gCost*= ICE_MULTIPLIER;
		}
		Coordinate coor = WorldSensor.getCurrentPosition();
		if (WorldSensor.car.getVelocity()==0) {
			Direction nb = absoluteToRelativePosition(current, neighbour);
			if (!WorldSensor.car.getOrientation().equals(nb)) {
				gCost *= TURN_MULTIPLIER;
			}
		}
		return gCost;
	}
	
public static Direction absoluteToRelativePosition(Coordinate current, Coordinate next) {
		
		Coordinate pos = new Coordinate(next.x - current.x, next.y - current.y);
		
		if (pos.x > 0) {
			return Direction.EAST;
		}
		else if (pos.y < 0) {
			return Direction.SOUTH;
			
		} else if (pos.x < 0) {
			return Direction.WEST;
			
		}else if (pos.y > 0) {
			return Direction.NORTH;
			
		} else {
			
			return null;
		}
	}
	
	private static ArrayList<Coordinate> getValidNeighbours(Coordinate current) {
		neighbourTiles = new HashMap<>();
		ArrayList<Coordinate> validNeighbours = new ArrayList<>();
		neighbourTiles = WorldSensor.getNeighbours(current);
		
		for (Coordinate neighbour: neighbourTiles.keySet()) {
			
			if (!worldMap.containsKey(neighbour)) {
				continue;
			}
				
			MapTile tile = worldMap.get(neighbour);
			if (tile.isType(MapTile.Type.WALL) || tile.isType(MapTile.Type.EMPTY) || tile instanceof MudTrap) {
				if (tile instanceof MudTrap) {
					continue;
				}
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

        path.pop();
        return path;
	}
	
	
	
	private static float getManhattanDistance(Coordinate from, Coordinate to) {
		return (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y));
	}

}
	


