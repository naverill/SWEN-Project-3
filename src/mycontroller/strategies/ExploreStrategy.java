package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy extends BasicStrategy {	
	ArrayList<Coordinate> unexploredTiles = new ArrayList<Coordinate>();
	
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		populateUnexploredTiles(map);
	}
	
	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		Coordinate exploreTile = path.getNextMove();
		
		if(path.endPath()) {
			path = new Path(WorldSensor.getCurrentPosition(), unexploredTiles, worldView);
		}
		
		return exploreTile;
	}
	
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(unexploredTiles.contains(coordinate)) {
				unexploredTiles.remove(coordinate);
			}
		}
		
        Collections.sort(unexploredTiles, new CoordinateComparator(WorldSensor.getCurrentPosition()));
	}
	
	
	class CoordinateComparator implements Comparator<Coordinate> {
		Coordinate currentPosition;
		
		CoordinateComparator(Coordinate position){
			this.currentPosition = position;
		}
		
		@Override
	    public int compare(Coordinate c1, Coordinate c2)
	    {	
			//compare points based on euclidean distance from current position
			return Double.compare(dist(currentPosition, c1), dist(currentPosition, c2));
	    }
		
		private double dist(Coordinate c1, Coordinate c2) {
			return Math.sqrt(Math.pow((c1.x - c2.x), 2) + Math.pow((c1.y - c2.y), 2));
		}
	};
	
	
	private void populateUnexploredTiles(HashMap<Coordinate, MapTile> map) {
		for(Coordinate coordinate : map.keySet()) {
			MapTile tile = map.get(coordinate);
			
			if(tile.getType().equals(MapTile.Type.ROAD) || tile.getType().equals(MapTile.Type.TRAP)) {
				//TODO() if tile has a path to it (not blocked by mud/walls)
				unexploredTiles.add(coordinate);
				
			}
		}
        Collections.sort(unexploredTiles, new CoordinateComparator(WorldSensor.getCurrentPosition()));
	}
}
