package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class Path {
	public Stack<Coordinate> pathMoves = new Stack<>();
	
	public Path() {}

	public Path(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles) {
		pathMoves = getPath(start, end, tiles);
	}
	
	
	public Coordinate getNextMove(){
		return pathMoves.pop();
	}
	
	private Stack<Coordinate> getPath(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles){		
		return pathMoves;
	}


	public boolean endPath() {
		return pathMoves.isEmpty();
	}
}
