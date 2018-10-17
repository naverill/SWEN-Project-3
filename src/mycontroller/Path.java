package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

public class Path {
	public Stack<Coordinate> pathCoordinates = new Stack<>();
		
	public Path() {}

	public Path(HashMap<Coordinate, MapTile> tiles, Coordinate start, ArrayList<Coordinate> end) {
		pathCoordinates = getPath(tiles, start, end);
	}
	
	
	public Coordinate getNextMove(){
		if(pathCoordinates.empty()) {
			
			return invalid;
		}
		//System.out.println(pathCoordinates);
		
		return pathCoordinates.pop();
	}
	
	private Stack<Coordinate> getPath( HashMap<Coordinate, MapTile> tiles, Coordinate start, ArrayList<Coordinate> end){		
		Pair<Stack<Coordinate>, Float> currCost;
		
		Pair<Stack<Coordinate>, Float> minCost = new Pair<>(new Stack<>(), Float.MAX_VALUE);

		for(Coordinate coordinate : end) {
			currCost = AStarSearch.findBestPath(tiles, start, start, coordinate);
			
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
		//System.out.println(minCost.getFirst());
		return minCost.getFirst();
	}


	public boolean endPath() {
		return pathCoordinates.isEmpty();
	}
	
	public void clearPath() {
		pathCoordinates.clear();
	}
	
	public static boolean invalidMove(Coordinate c) {
		return c.equals(invalid) || invalidXCoordinate(c) || invalidYCoordinate(c);
	}
	
	public static boolean invalidXCoordinate(Coordinate c) {
		return (c.x > World.MAP_WIDTH) || (c.x < 0);
	}
	
	public static boolean invalidYCoordinate(Coordinate c) {
		return (c.y > World.MAP_HEIGHT) || (c.y < 0);
	}
	
	public static final Coordinate invalid = new Coordinate(-1, -1);
}
