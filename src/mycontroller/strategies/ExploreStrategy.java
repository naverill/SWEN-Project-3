package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import mycontroller.CarSensor;
import mycontroller.util.Move;
import mycontroller.util.Path;
import tiles.MapTile;
import tiles.MudTrap;
import utilities.Coordinate;


/**
 * Extends the abstract class BasicStrategy. The ExploreStrategy is responsible 
 * for exploring all tiles on the map. The strategy generates and follows
 * the least cost path to a unexplored tile outside the car's range of vision. 
 **/
public class ExploreStrategy extends BasicStrategy {
	static final int NODES_OUTSIDE_VIEW = 40; //number of nodes bordering view of Car
	
	/**
	 * The ExploreStrategy
	 * @param map - the current known map
	 **/
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		populateUnexploredTiles(map);
	}
	
	
	/**
	 * Return the next move in the current optimal path to the goal tile
	 * @param map - the current known map
	 **/
	@Override
	public Move move() {
		//if goal tile is reached, recalculate next path 
		if(path.endPath()) {
			path = potentialPath();
		}
		
		Coordinate nextMove = path.getNextMove();		
		return new Move(nextMove);
	}
	
	/**
	 * UpdateState() is responsible for reading in the current view of the car, removing 
	 * explored tiles as they are encountered
	 * @param view - the current view of the car 
	 **/
	public void updateState(HashMap<Coordinate, MapTile> view) {
		for(Coordinate coordinate : view.keySet()) {
			if(goal.contains(coordinate)) {
				goal.remove(coordinate);
			}
		}
		
		//ensure current path does not intersect with any trap tiles 
		checkCurrentPath(view);
		//sort goal tiles according to closest location to current position
        Collections.sort(goal, new CoordinateComparator(CarSensor.getCurrentPosition()));
	}
	
	/**
	 * This is responsible for checking and recalculating the current optimal pat  if traps are
	 *  encountered.
	 *  @param map - the current world map
	 **/
	public void checkCurrentPath(HashMap<Coordinate, MapTile> map) {
		for(Coordinate coordinate: path.getCurrentPath()) {
			if(map.containsKey(coordinate)){
				if(map.get(coordinate) instanceof MudTrap) {
					path = potentialPath();
				}
			}
		}
	}
	
	/**
	 * A comparator to sort Coordinates according to the euclidean distance from 
	 * the current car position
	 **/
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
		
		//distance from current tile 
		private double dist(Coordinate c1, Coordinate c2) {
			return Math.sqrt(Math.pow((c1.x - c2.x), 2) + Math.pow((c1.y - c2.y), 2));
		}
	};
	

	/**
	 * This is responsible for populating the goal array with all reachable tiles
	 * @param map - the current world map
	 **/
	private void populateUnexploredTiles(HashMap<Coordinate, MapTile> map) {
		//System.out.println(map);
		for(Coordinate coordinate : map.keySet()) {
			MapTile tile = map.get(coordinate);
			
			if(tile.getType().equals(MapTile.Type.ROAD) || tile.getType().equals(MapTile.Type.TRAP)) {
				
					if(Path.hasPath(coordinate)) {
						goal.add(coordinate);
					}		
			}
		}
        Collections.sort(goal, new CoordinateComparator(CarSensor.getCurrentPosition()));
	}
	
	/**
	 * This is responsible for recalculating the optimal path
	 * @param map - the current world map
	 **/
	@Override
	public void reset() {
		path.clearPath();
		if (goal.size() > NODES_OUTSIDE_VIEW) {
			path = new Path(CarSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
		}
		else {
			path = new Path(CarSensor.getCurrentPosition(), goal);
		}	
	}
	
	/**
	 * Returns an optimal path to a goal node 
	 * @param map - the current world map
	 **/
	@Override
	public Path potentialPath() {
		if (goal.size() > NODES_OUTSIDE_VIEW) {
			return new Path(CarSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
		} else {
			return new Path(CarSensor.getCurrentPosition(), goal);
		}	
	}
	
}
