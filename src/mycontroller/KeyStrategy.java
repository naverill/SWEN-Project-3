package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public class KeyStrategy implements IMovementStrategy {
	ArrayList<Coordinate> keys;
	Path keyPath;

	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile.Type> worldView) {
		// TODO Auto-generated method stub
		
		if(keyPath.isEmpty()) {
			keyPath = new Path(currentPos, keys, worldView);
		} 
		
		return keyPath.getNextMove();
	}
	
	public boolean foundKey() {
		return !keys.isEmpty();
	}

}
