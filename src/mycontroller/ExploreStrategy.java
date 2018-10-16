package mycontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		for(Coordinate coordinate : state.keySet()) {
			if(unexploredTiles.contains(coordinate)) {
				unexploredTiles.remove(coordinate);
			}
		}
		
        Collections.sort(unexploredTiles, new CoordinateSort());
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
	
	class CoordinateSort implements Comparator<Coordinate> {
		Coordinate currentPosition;
		
		CoordinateSort(Coordinate position){
			this.currentPosition = position;
		}
		
		@Override
	    public int compare(Coordinate c1, Coordinate c2)
	    {	
			//sort according to closest x value
	        if (Math.abs(currentPosition.x - c1.x)  < Math.abs(currentPosition.x - c2.x)) {
	            return -1;
	        //if equal, sort according to closest y value 
	        } else if (Math.abs(currentPosition.x - c1.x) == Math.abs(currentPosition.x - c2.x)) {
	            return Math.abs(currentPosition.y - c1.y) - Math.abs(currentPosition.y - c2.y);
	        } else {
	            return 1;
	        }
	    }
	};
}
