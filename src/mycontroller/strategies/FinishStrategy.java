package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.util.Move;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

/**
 * Extends the abstract class BasicStrategy. The ExploreStrategy is responsible 
 * for navigating the car to the finish tile.
 **/
public class FinishStrategy extends BasicStrategy{

	/**
	 * Return the next move in the current optimal path to the finish tile
	 * @param map - the current known map
	 **/
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = potentialPath(worldView);
		}
		
		Coordinate nextMove = path.getNextMove();		
		return new Move(nextMove);	
	}

	
	/**
	 * UpdateState() is responsible for reading in the current view of the car, adding 
	 * finish tiles as they are encountered
	 * @param view - the current view of the car 
	 **/
	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate).getType().equals(Type.FINISH)) {
				if(!goal.contains(coordinate)) {
					goal.add(coordinate);
				}
			}
		}
	}
	
}
