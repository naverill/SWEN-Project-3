package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class PathFinder {
	public Stack<Coordinate> pathMoves;

	public PathFinder(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles) {
		pathMoves = getPath(start, end, tiles);
	}
	
	
	public Coordinate getNextMove(){
		return pathMoves.pop();
	}
	
	private Stack<Coordinate> getPath(Coordinate start, ArrayList<Coordinate> end, HashMap<Coordinate, MapTile> tiles){
		Stack<Coordinate> path = new Stack<>();
		
		return path;
	}


	public boolean isEmpty() {
		return pathMoves.isEmpty();
	}
}
