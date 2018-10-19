package mycontroller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import mycontroller.AStarSearch;
import mycontroller.CarSensor;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial.Direction;

/**
 * The Path class is responsible for generating the optimal path between a start coordinate and 
 * an end coordinate based on the current known conditions of the map
 * */
public class Path {
	private Stack<Coordinate> pathCoordinates = new Stack<>(); //coordinates of the path
		
	public Path() {}

	public Path(HashMap<Coordinate, MapTile> tiles, Coordinate start, ArrayList<Coordinate> end) {
		pathCoordinates = getPath(tiles, start, end);
	}
	
	/**
	 * Returns the coordinate of the next move in the path 
	 * */
	public Coordinate getNextMove(){
		//return invalid coordinate if path is empty
		if(pathCoordinates.empty()) {
			return INVALID;
		}		
		return pathCoordinates.pop();
	}
	
	/**
	 * getPath is responsible for finding the minimum cost-path of all possible paths to
	 * an array of end tiles.
	 * @param map - the current map of the world
	 * @param start - the start coordinate
	 * @param end - list of possible end points of the paths 
	 * */
	private Stack<Coordinate> getPath(HashMap<Coordinate, MapTile> map, Coordinate start, ArrayList<Coordinate> end){		
		Pair<Stack<Coordinate>, Float> currCost;
		Pair<Stack<Coordinate>, Float> minCost = new Pair<>(new Stack<>(), Float.MAX_VALUE);

		//find least cost path to all coordinates
		for(Coordinate coordinate : end) {
			currCost = AStarSearch.findBestPath(map, start, start, coordinate);
			
			//find the minimum cost path
			if (currCost != null) {
				if(currCost.getSecond() < minCost.getSecond()) {
					minCost.setFirst(currCost.getFirst());
					minCost.setSecond(currCost.getSecond());
				}
			}
			else {
				continue;
			}
			
		}
		return minCost.getFirst();
	}

	/**
	 * Returns boolean value indicating if end of path is complete
	 * */
	public boolean endPath() {
		return pathCoordinates.isEmpty();
	}
	
	/**
	 * Returns current optimal path
	 * */
	public Stack<Coordinate> getCurrentPath() {
		return pathCoordinates;
	}
	
	/**
	 * empties current path
	 * */
	public void clearPath() {
		pathCoordinates.clear();
	}
	
	/**
	 * adds a node to the current path
	 * @param coordinate - coordinate of move to push onto path stack 
	 * */
	public void addPathNode(Coordinate coordinate) {
		pathCoordinates.push(coordinate);
	}
	
	/**
	 * returns boolean value indicating if move is invalid
	 * @param move - move to be evaluated
	 * */
	public static boolean invalidMove(Move move) {
		return move.getTarget().equals(INVALID) || invalidXCoordinate(move.getTarget()) || invalidYCoordinate(move.getTarget()) || notCardinalMovement(move);
	}
	
	/**
	 * returns boolean value indicating if move is in cardinal direction (north, south, east, west)
	 * @param move - move to be evaluated
	 * */
	public static boolean notCardinalMovement(Move move) {
		return absoluteToCardinal(move.getCurrent(), move.getTarget())==null;
	}
	
	/**
	 * returns boolean value indicating if x coordinate is invalid
	 * @param c - coordinate to be evaluated
	 * */
	public static boolean invalidXCoordinate(Coordinate c) {
		return (c.x > World.MAP_WIDTH) || (c.x < 0);
	}
	
	/**
	 * returns boolean value indicating if y coordinate is invalid
	 * @param c - coordinate to be evaluated
	 * */
	public static boolean invalidYCoordinate(Coordinate c) {
		return (c.y > World.MAP_HEIGHT) || (c.y < 0);
	}
	
	
	/**
	 * Returns Direction of next target in relation to current 
	 * @param current - current coordinate of car
	 * @param next - next coordinate of car
	 * */
	public static Direction absoluteToCardinal(Coordinate current, Coordinate next) {
		
		Coordinate delta = new Coordinate(next.x - current.x, next.y - current.y);
		
		if(delta.equals(NORTH)) {
			return Direction.NORTH;
			
		} else if (delta.equals(SOUTH)) {
			return Direction.SOUTH;
			
		} else if (delta.equals(EAST)) {
			return Direction.EAST;
			
		}else if (delta.equals(WEST)) {
			return Direction.WEST;
			
		}else if (delta.equals(ORIGIN)) {
			//car is stationary, so return current orientation
			return CarSensor.getOrientation();
			
		} else {
			
			return null;
		}
	}
	
	/**
	 * Returns boolean value indicating if there exists a path to a coordinate
	 * @param coorinate - coordinate to be evaluated
	 * */
	public static boolean hasPath(Coordinate coordinate) {
		return AStarSearch.findBestPath(CarSensor.getWorldMap(), CarSensor.getCurrentPosition(), CarSensor.getCurrentPosition(), coordinate)!=null;
	}
	
	/**
	 * Delta values of all cardinal directions
	 * */
	private static final Coordinate NORTH = new Coordinate(0, 1);
	private static final Coordinate EAST = new Coordinate(1, 0);
	private static final Coordinate SOUTH = new Coordinate(0, -1);
	private static final Coordinate WEST = new Coordinate(-1, 0);
	private static final Coordinate ORIGIN = new Coordinate(0, 0);
	public static final Coordinate INVALID = new Coordinate(-1, -1);
}
