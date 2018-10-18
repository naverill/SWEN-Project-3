package mycontroller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial.Direction;

public class WorldSensor {
	public static HashMap<Coordinate, MapTile> map = new HashMap<>();
	public static Car car;
	public static final int LAVA_COST = 5;
	public static final int DANGER_RANGE = 20;
	private static Set<Integer> keysSeen = new HashSet<>();
	
	public WorldSensor(HashMap<Coordinate, MapTile> worldTiles, Car car) {
		this.car = car;
		map.putAll(worldTiles);
	}

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
	
	public static void updateMap(HashMap<Coordinate, MapTile> view) {
		map.putAll(view);
	}
	
	public static HashMap<Coordinate, MapTile> getWorldMap() {
		return map;
	}
	
	public static boolean hasAllKeys() {		
		return keysSeen.equals(car.getKeys());
	}
	
	public static Coordinate getCurrentPosition() {
		return new Coordinate(car.getPosition());
	}
	
	static public Direction getOrientation() {
		return car.getOrientation();
	}
	
	static public int getVelocity() {
		return car.getVelocity();
	}
	
	public static MapTile getTileAtCoordinate(Coordinate coordinate) {
		return map.get(coordinate);
	}
	
	public static boolean isTrap(MapTile tile) {
		return tile instanceof LavaTrap;
	}
	
	public static boolean isHealth(MapTile tile) {
		return tile instanceof HealthTrap;
	}
	
	public static boolean isStart(MapTile tile) {
		return tile.getType().equals(MapTile.Type.START);
	}
	
	public static boolean hasEnoughHealth(Stack<Coordinate> path) {
		float currentHealth = car.getHealth();
		float healthBuffer = 0;
		for(Coordinate coor: path) {
			if(map.get(coor) instanceof LavaTrap) {
				healthBuffer += LAVA_COST;
			}
		}
		System.out.println(healthBuffer);
		return currentHealth > healthBuffer;
	}
	
	//HIGHWAY TO THEDANGERZONE
	public static boolean nearCriticalLowHealth(Stack<Coordinate> path) {
		//move to healthTile before its too late
		float currentHealth = car.getHealth() - DANGER_RANGE;
		
		float healthBuffer = 0;
		for(Coordinate coor: path) {
			if(map.get(coor) instanceof LavaTrap) {
				healthBuffer+=LAVA_COST;
			}
		}
		return currentHealth <= healthBuffer;
	}
	
	public static boolean isHealing() {
		return map.get(getCurrentPosition()) instanceof HealthTrap;
	}
	
	public static boolean isDoneHealing() {
		return car.getHealth() == 100.0f;
	}
	
	public static void addKey(int num) {
		keysSeen.add(num);
	}

}
