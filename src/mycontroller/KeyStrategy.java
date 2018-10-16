package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy implements IMovementStrategy {
	ArrayList<Coordinate> keys;
	Path keyPath;

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		// TODO Auto-generated method stub
		
		if(keyPath.isEmpty()) {
			keyPath = new Path(null, keys, worldView);
		} 
		
		return keyPath.getNextMove();
	}
	
	public boolean foundKey() {
		return !keys.isEmpty();
	}

}
