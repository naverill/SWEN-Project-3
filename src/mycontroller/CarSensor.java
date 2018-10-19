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

/**
 * The CarSensor class stores and processes all world knowledge collected by the car
 * */
public class CarSensor {
	private static HashMap<Coordinate, MapTile> map = new HashMap<>(); //map of all world Tiles
	private static Car car;
	public static final int LAVA_COST = 5; //health cost of lava tiles
	public static final int DANGER_RANGE = 20; // health buffer for car 
	private static Set<Integer> keys = new HashSet<>(); //set of all seens keys in the map
	
	public CarSensor(HashMap<Coordinate, MapTile> worldTiles, Car car) {
		this.car = car;
		updateMap(worldTiles);
	}
	
	/**
	 * Returns if the car has enough health to traverse a path in the map
	 * @param path - a path of coordinates to traverse
	 **/
	public static boolean hasEnoughHealth(Stack<Coordinate> path) {
		float currentHealth = car.getHealth();
		float healthBuffer = 0;
		for(Coordinate coor: path) {
			if(map.get(coor) instanceof LavaTrap) {
				healthBuffer += LAVA_COST;
			}
		}
		return currentHealth > healthBuffer;
	}
	
	/**
	 * Returns if the car is nearing critical low health to traverse a path 
	 * @param path - a path of coordinates to traverse
	 **/
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
	
	/**
	 * Returns a boolean indicating if the car is blocked by an adjacent tile in opposite directions
	 * @param coordinate - the current coordinate
	 * @param direction - the current direction the car is facing  
	 **/
	public static boolean isBlocked(Coordinate coordinate, Direction direction) {
		Coordinate delta = car.directionDelta(direction); //coordinate change associated with direction
		Coordinate adjacentCoordinate = new Coordinate(coordinate.x + delta.x, coordinate.y + delta.y);
		
		MapTile adjacentTile = getTileAtCoordinate(adjacentCoordinate);
		
		return adjacentTile.isType(MapTile.Type.WALL);	
		
	}
	
	/**
	 * Updates map with world information
	 * @param view - the car's current view of the map
	 * */
	public static void updateMap(HashMap<Coordinate, MapTile> view) {
		map.putAll(view);
	}
	
	/**
	 * returns HashMap of all world tiles 
	 * */
	public static HashMap<Coordinate, MapTile> getWorldMap() {
		return map;
	}
	
	
	/**
	 * Returns a boolean if all unique keys in the map have been collected by the car 
	 * */
	public static boolean hasAllKeys() {		
		return keys.equals(car.getKeys());
	}
	
	/**
	 * Returns the current position of the car
	 * */
	public static Coordinate getCurrentPosition() {
		return new Coordinate(car.getPosition());
	}
	
	/**
	 * Returns the current orientation of the car
	 * */
	static public Direction getOrientation() {
		return car.getOrientation();
	}
	
	/**
	 * Returns the current velocity of the car
	 * */
	static public int getVelocity() {
		return car.getVelocity();
	}
	
	/**
	 * Returns the tile at a specified coordinate
	 * @param coordinate - the desired coordinate
	 * */
	public static MapTile getTileAtCoordinate(Coordinate coordinate) {
		return map.get(coordinate);
	}
	
	/**
	 * Returns if the tile at a specified coordinate is Lava
	 **/
	public static boolean isTrap(MapTile tile) {
		return tile instanceof LavaTrap;
	}
	
	/**
	 * Returns if the tile at a specified coordinate is Health
	 **/
	public static boolean isHealth(MapTile tile) {
		return tile instanceof HealthTrap;
	}
	
	/**
	 * Returns if the tile at a specified coordinate is the Start tile
	 **/
	public static boolean isStart(MapTile tile) {
		return tile.getType().equals(MapTile.Type.START);
	}
	
	/**
	 * Returns if the car is currently stationed on a Health tile
	 **/
	public static boolean isHealing() {
		return map.get(getCurrentPosition()) instanceof HealthTrap;
	}
	
	/**
	 * Returns if the car is at full health
	 **/
	public static boolean isDoneHealing() {
		return car.getHealth() == 100.0f;
	}
	
	/**
	 * Adds a key to set of collected keys 
	 * @param num - the key number 
	 **/
	public static void addKey(int num) {
		keys.add(num);
	}
	
	public static Set<Integer> getSeenKeys() {		
		return keys;
	}
	
	public static Set<Integer> getCollectedKeys(){
		return car.getKeys();
	}
	

}
