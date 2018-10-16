package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class ExploreStrategy implements IMovementStrategy {	
	ArrayList<Coordinate> unexploredTiles = new ArrayList<Coordinate>();

	
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		populateUnexploredTiles(map);
	}
	
	@Override
	public Coordinate move(Direction currentDirection, Coordinate currentPos, HashMap<Coordinate, MapTile> worldView) {		
		
	}
	
	public void updateState(HashMap<Coordinate, MapTile> state) {
		
	}
	
	
	private void populateUnexploredTiles(HashMap<Coordinate, MapTile> map) {
		for(Coordinate coordinate : map.keySet()) {
			MapTile tile = map.get(coordinate);
			
			if(tile.getType().equals(MapTile.Type.ROAD) || tile.getType().equals(MapTile.Type.TRAP)) {
				//TODO() if tile has a path to it (not blocked by mud/walls)
				unexploredTiles.add(coordinate);
				
			}
		}
		
	}


}
