package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import mycontroller.AStarSearch;
import mycontroller.Move;
import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy extends BasicStrategy {
	static final int NODES_OUTSIDE_VIEW = 40; //number of nodes bordering view of Car
	
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		populateUnexploredTiles(map);
	}
	
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			if (goal.size() > NODES_OUTSIDE_VIEW) {
				path = new Path(worldView, WorldSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
			}
			else {
				path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
			}
			
		}
		
		Coordinate nextMove = path.getNextMove();
		MapTile nextTile = WorldSensor.getTileAtCoordinate(nextMove);
		Move.Acceleration acceleration = adjustAcceleration(nextTile);
		
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(nextMove, acceleration);
	}
	
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(goal.contains(coordinate)) {
				goal.remove(coordinate);
			}
		}
		
        Collections.sort(goal, new CoordinateComparator(WorldSensor.getCurrentPosition()));
        System.out.println(goal);
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
	
	
	//adds unexplored tiles to goal array
	private void populateUnexploredTiles(HashMap<Coordinate, MapTile> map) {
		Coordinate currentPosition = WorldSensor.getCurrentPosition();
		for(Coordinate coordinate : map.keySet()) {
			MapTile tile = map.get(coordinate);
			
			if(tile.getType().equals(MapTile.Type.ROAD) || tile.getType().equals(MapTile.Type.TRAP)) {
				if(AStarSearch.findBestPath(map, currentPosition, currentPosition, coordinate)!=null) {
					goal.add(coordinate);
				}
			}
		}
        Collections.sort(goal, new CoordinateComparator(WorldSensor.getCurrentPosition()));
	}
	
	@Override
	public void reset(HashMap<Coordinate, MapTile> map) {
		path.clearPath();
		if (goal.size() > NODES_OUTSIDE_VIEW) {
			path = new Path(map, WorldSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
		}
		else {
			path = new Path(map, WorldSensor.getCurrentPosition(), goal);
		}	
	}
	
}
