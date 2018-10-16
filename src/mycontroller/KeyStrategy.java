package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public class KeyStrategy implements IMovementStrategy {
	static private ArrayList<Coordinate> keys;
	Path keyPath;

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		// TODO Auto-generated method stub
		
		if(keyPath.isEmpty()) {
			keyPath = new Path(WorldSensor.getCurrentPosition(), keys, worldView);
		} 
		
		return keyPath.getNextMove();
	}
	
	public boolean foundKey() {
		return !keys.isEmpty();
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			MapTile tile = state.get(coordinate);
			if(tile instanceof LavaTrap) {
				if(((LavaTrap) tile).getKey() != 0){
					keys.add(coordinate);
				} 
			}
		}
	}
	

}
