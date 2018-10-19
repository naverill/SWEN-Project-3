package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.util.Move;
import tiles.MapTile;
import utilities.Coordinate;


/**
 * This interface facilitates the traversal strategies. The class provides a way for the caller
 * to generate paths to possible coordinates based on defined goals.
 */
public interface IMovementStrategy {

	/**
	 * This outputs the next possible movement for the car
	* @param map - the world map of all known tiles  
	 */
	public Move move();
	
	/**
	 * This updates the world state that defines the decision making of the strategy
	 * @param view  - the current view of the car 
	 */
	public void updateState(HashMap<Coordinate, MapTile> view);
	
	/**
	* Reset the strategy and recalculate it based on the current world conditions
	* @param map - the world map of all known tiles  
	*/
	public void reset();

}

