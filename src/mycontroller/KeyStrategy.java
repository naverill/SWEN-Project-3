package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy extends BasicStrategy {
	static private ArrayList<Coordinate> keys;
	ArrayList<Coordinate> collected = new ArrayList<>();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		Coordinate keyTile = path.getNextMove();
		
		if(path.endPath()) {
			collected.add(keyTile);
			path = new Path(WorldSensor.getCurrentPosition(), keys, worldView);
		} 
		
		return path.getNextMove();
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
