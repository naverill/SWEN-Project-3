package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import mycontroller.CarSensor;
import mycontroller.util.Move;
import mycontroller.util.Path;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

/**
 * Extends the abstract class BasicStrategy. The KeyStrategy is responsible for 
 * identifying the least-cost path to a key tile. 
 **/
public class KeyStrategy extends BasicStrategy {	
	/**
	 * Return the next move in the current optimal path to the goal tile
	 * @param map - the current known map
	 **/
	@Override
	public Move move() {		
		if(path.endPath()) {
			path = potentialPath();
		} 

		Coordinate nextMove = path.getNextMove();		
		return new Move(nextMove);
	}

	/**
	 * UpdateState() is responsible for continuously reading in the current view of the car, 
	 * removing explored tiles as they are encountered
	 * @param view - the current view of the car 
	 **/
	@Override
	public void updateState(HashMap<Coordinate, MapTile> view) {
		for(Coordinate coordinate : view.keySet()) {
			MapTile tile = view.get(coordinate);
			
			if(tile instanceof LavaTrap) {
				//add key to goal if this is the first time it has been seen 
				if((isKey((LavaTrap) tile)) && !foundKey(coordinate)){
					//if key is accessible from a road 
					if(!collected(((LavaTrap) tile).getKey()) && Path.hasPath(coordinate)) {
						goal.add(coordinate);
					}
				} 
			}
		}
	}
	
	/**
	 * Returns a boolean value indicating if a lava tile contains a key
	 * @param tile - lava tile to be evaluated
	 **/
	public boolean isKey(LavaTrap tile) {
		int keyNum = tile.getKey();
		if(keyNum !=0) {
			CarSensor.addKey(keyNum);
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a boolean value indicating if a key has previously come across 
	 * the key in the map
	 * @param tile - lava tile to be evaluated
	 **/
	public boolean foundKey(Coordinate key) {
		return goal.contains(key);
	}
	
	/**
	 * Returns a boolean value if car has collected the key at a specified coordinate
	 * @param key - key tile to be assessed
	 **/
	private boolean collected(Integer key) {
		return CarSensor.getCollectedKeys().contains(key);
	}
}
