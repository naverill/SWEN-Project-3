package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.CarSensor;
import mycontroller.util.Move;
import mycontroller.util.Path;
import tiles.MapTile;
import utilities.Coordinate;

/**
* This abstract class class is forms the superclass for all basic movement strategies. 
* This class of strategies are responsible for generating optimal paths to a defined 
* class of goal tile. This path is calculated based on factors such as distance, health etc. 
*  Once this path is complete, the next optimal path is generated, updating the 
*  state of goal tiles as the car traverses the map.
*/
public abstract class BasicStrategy implements IMovementStrategy{
	protected Path path = new Path();
	protected ArrayList<Coordinate> goal = new ArrayList<>();

	/**
	* Abstract function that defines how the next movement in the path is handled
	* @param map - the world map of all known tiles
	*/
	@Override
	public abstract Move move();

	/**
	* Abstract function that handles updating the state of the strategy based on world conditions
	* @param view - the current view of the car 
	*/
	@Override
	public abstract void updateState(HashMap<Coordinate, MapTile> view);
	
	/**
	* Returns if the car has found a goal tile in the world map
	*/
	public boolean foundGoalTile() {
		return !goal.isEmpty();
	}
	
	/**
	* Reset the strategy and recalculate it based on the current world conditions
	* @param map - the world map of all known tiles  
	*/
	public void reset(HashMap<Coordinate, MapTile> map) {
		path.clearPath();
		path = new Path(CarSensor.getCurrentPosition(), goal);	
	}
	
	/**
	* A possible path that the car can follow based on current world conditions
	* @worldView  - the current view of the car
	*/
	public Path potentialPath() {
		Coordinate currentPosition = CarSensor.getCurrentPosition();
		return new Path(currentPosition, goal);
	}

}
