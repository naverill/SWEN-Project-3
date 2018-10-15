package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy implements IMovementStrategy {
	ArrayList<Coordinate> keys;
	Stack<Coordinate> keyPath = new Stack<>();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		// TODO Auto-generated method stub
		
		if(keyPath.isEmpty()) {
			keyPath = new Path();
		} 
		
		return keyPath.pop();
	}
	
	public boolean foundKey() {
		return !key.isEmpty();
	}

}
