package mycontroller.strategies;

import java.util.HashMap;
import mycontroller.CarSensor;
import mycontroller.util.Move;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;


/**
 * Extends the abstract class BasicStrategy. The HealthStrategy is responsible for 
 * identifying the least-cost path to a health tile. The strategy is also responsible for 
 * defining the car's behaviour while healing.
 **/
public class HealthStrategy extends BasicStrategy {
	/**
	 * Return the next move in the current optimal path to a health tile
	 * @param map - the current known map
	 **/
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {	
		//if goal tile is reached, recalculate next path 
		if(path.endPath()) {
			path = potentialPath(worldView);
		}
		
		//if car is located on a healing tile, brake on tile until healing is complete
		if(currentlyHealing() && !fullyHealed()) {
			return new Move(CarSensor.getCurrentPosition());
		}
		
		Coordinate nextMove = path.getNextMove();
		return new Move(nextMove);
	}

	/**
	 * UpdateState() is responsible for reading in the current view of the car, adding 
	 * health tiles as they are encountered
	 * @param view - the current view of the car 
	 **/
	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate) instanceof HealthTrap) {
				if(!goal.contains(coordinate)) {
					goal.add(coordinate);
				}
			}
		}
	}
	
	/**
	 * Returns if the car is currently located on a health tile
	 **/
	public boolean currentlyHealing() {
		return CarSensor.isHealing();
	}
	
	/**
	 * Returns if the car is at full health
	 **/
	public boolean fullyHealed() {
		return CarSensor.isDoneHealing();
	}

}
