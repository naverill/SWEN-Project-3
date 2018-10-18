package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.AStarSearch;
import mycontroller.Move;
import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy extends BasicStrategy {
	protected ArrayList<Coordinate> collected = new ArrayList<>();

	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {
		collectKey();
		
		if(path.endPath()) {
			path = potentialPath(worldView);
		} 
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(path.getNextMove(), Move.Acceleration.ACCELERATE);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			MapTile tile = state.get(coordinate);
			
			if(tile instanceof LavaTrap) {
				if((isKey((LavaTrap) tile)) && !foundKey(coordinate)){
					if(!collected(coordinate) && Path.hasPath(coordinate)) {
						goal.add(coordinate);
					}
				} 
			}
		}
	}
	
	public boolean isKey(LavaTrap tile) {
		return tile.getKey()!=0;
	}
	
	public boolean foundKey(Coordinate key) {
		return goal.contains(key);
	}
	
	private boolean collected(Coordinate coordinate) {
		return collected.contains(coordinate);
	}
	
	private void collectKey() {
		Coordinate currentPosition = WorldSensor.getCurrentPosition();
		if(goal.contains(currentPosition)) {
			collected.add(currentPosition);
			goal.remove(currentPosition);
		}
	}
}
