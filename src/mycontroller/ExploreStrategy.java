package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy implements IMovementStrategy {
	private HashMap<Coordinate, Boolean> explored;
	
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		for(Coordinate key : map.keySet()) {
			explored.put(key, false);
		}
	}

	@Override
	public void move(HashMap<Coordinate, MapTile> worldView) {
		
	}

}
