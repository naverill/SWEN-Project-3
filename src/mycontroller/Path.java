package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

public class Path {
	public Stack<Coordinate> pathMoves = new Stack<>();
		
	public Path() {}

	public Path(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles) {
		pathMoves = getPath(start, end, tiles);
	}
	
	
	public Coordinate getNextMove(){
		if(pathMoves.empty()) {
			return invalid;
		}
		return pathMoves.pop();
	}
	
	private Stack<Coordinate> getPath(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles){		
		return pathMoves;
	}


	public boolean endPath() {
		return pathMoves.isEmpty();
	}
	
	public void clearPath() {
		pathMoves.clear();
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
