package mycontroller;

import java.util.*;



import mycontroller.util.Pair;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
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
	
	private static final float LAVA_MULTIPLIER = 100.0f;
	private static final float ICE_MULTIPLIER = 0.5f;
	private static final float OTW_MULTIPLIER = 0.5f;
	private static final float GRASS_MULTIPLIER = Float.MAX_VALUE;

	public static Pair<Stack<Coordinate>, Float> findBestPath(Coordinate iBeforeStart,
            Coordinate iStart, Coordinate iGoal) {

		map = CarSensor.getWorldMap();
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
			
			
			
			ArrayList<Coordinate> neighbours = getValidNeighbours(curr, cameFrom.get(curr));

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
		Direction nb = absoluteToRelativePosition(current, neighbour);
		Direction now = CarSensor.getOrientation();
		Direction rnb = WorldSpatial.reverseDirection(nb);
		//TODO lava tile turning cost
		//add trap multipliers if needed
		MapTile tile = map.get(neighbour);
		if (tile instanceof LavaTrap) {
			if(((LavaTrap) tile).getKey()!=0) {
				if(!CarSensor.getCollectedKeys().contains(((LavaTrap) tile).getKey())) {
					//doesnt have key then go through it
					gCost *= OTW_MULTIPLIER;
				}else {
					//does have key then go through it
					gCost *= LAVA_MULTIPLIER;
				}
			} else {
				gCost *= LAVA_MULTIPLIER;		
			}
		}
		if(tile instanceof HealthTrap) {
			gCost*= ICE_MULTIPLIER;
		}
		
		if ((tile instanceof GrassTrap) && needsToTurn(now, nb, rnb)) {
			gCost *= GRASS_MULTIPLIER;
		}
		
		

		if (CarSensor.getVelocity() == 0) {
			
			
			
			
//			if (!(now.equals(nb))) {
//				gCost *= TURN_MULTIPLIER;
//			}

//			if (needsToTurn(now, nb, rnb)) {
//				gCost *= TURN_MULTIPLIER;
//			}
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

	/**
	 * Getneighbours() take a tile coordinate and returns a HashMap of all neighbouring tiles
	 * @param key - the coordinate of the centre tile
	 * */
	public  static HashMap<Coordinate, MapTile> getNeighbours(Coordinate key){
		HashMap<Coordinate, MapTile> neighbours = new HashMap<>();
		int xValue = key.x;
		int yValue = key.y;
				
		Coordinate northNeighbourCoor = new Coordinate(xValue, yValue+1);
		Coordinate eastNeighbourCoor = new Coordinate(xValue+1, yValue);
		Coordinate westNeighbourCoor = new Coordinate(xValue-1, yValue);
		Coordinate southNeighbourCoor = new Coordinate(xValue, yValue-1);
		
		MapTile northNeighbourType = map.get(northNeighbourCoor);
		MapTile eastNeighbourType = map.get(eastNeighbourCoor);
		MapTile westNeighbourType = map.get(westNeighbourCoor);
		MapTile southNeighbourType = map.get(southNeighbourCoor);
	
		neighbours.put(northNeighbourCoor, northNeighbourType);
		neighbours.put(eastNeighbourCoor, eastNeighbourType);
		neighbours.put(westNeighbourCoor, westNeighbourType);
		neighbours.put(southNeighbourCoor, southNeighbourType);
		
		return neighbours;
	}

	
	private static ArrayList<Coordinate> getValidNeighbours(Coordinate current, Coordinate cameFrom) {
		neighbourTiles = new HashMap<>();
		ArrayList<Coordinate> validNeighbours = new ArrayList<>();
		neighbourTiles = getNeighbours(current);
		
		for (Coordinate neighbour: neighbourTiles.keySet()) {
			
			if (!map.containsKey(neighbour)) {
				continue;
			}
				
			MapTile tile = map.get(neighbour);
			if (tile.isType(MapTile.Type.WALL) || tile.isType(MapTile.Type.EMPTY) || tile instanceof MudTrap) {
				if (tile instanceof MudTrap) {
					continue;
				}
				continue;
			}
			
			if ((cameFrom == null) && CarSensor.getVelocity() == 0) {
				
				
					Direction nb = absoluteToRelativePosition(current, neighbour);
					Direction rnb = WorldSpatial.reverseDirection(nb);
					
					Direction nowOrient = CarSensor.getOrientation();
					
					if (needsToTurn(nowOrient, nb, rnb)) {
						continue;
					}				
			}
					
			validNeighbours.add(neighbour);	
		}
		return validNeighbours;
}
	
	public static boolean needsToTurn(Direction now, Direction to, Direction revTo) {
		
		if (now.equals(to)) {
			return false;
		}
		
		if (now.equals(revTo)) {
			return false;
		}
		return true;
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