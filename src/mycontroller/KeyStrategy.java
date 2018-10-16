package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public class KeyStrategy implements IMovementStrategy {
	static ArrayList<Coordinate> keys = new ArrayList<>();;
	Path keyPath;

	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile.Type> worldView) {
		// TODO Auto-generated method stub
		
		if(keyPath.isEmpty()) {
			keyPath = new Path(currentPos, keys, worldView);
		} 
		
		return keyPath.getNextMove();
	}
	
	public static void addKey(Coordinate key) {
		if(!keys.contains(key)) {
			keys.add(key);
		}
	}
	
	public void removeKey(Coordinate key) {
		keys.remove(key);
	}
	
//	public boolean foundKey() {
//		return !keys.isEmpty();
//	}
}
