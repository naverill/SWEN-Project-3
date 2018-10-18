package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
		Coordinate nextMove = path.getNextMove();
		
		MapTile nextTile = WorldSensor.getTileAtCoordinate(nextMove);
		
		Move.Acceleration acceleration = adjustAcceleration(nextTile);
		System.out.println(WorldSensor.getCurrentPosition());
		System.out.println(nextMove);
		System.out.println(path.getCurrentPath());
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(nextMove, acceleration);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			MapTile tile = state.get(coordinate);
			
			if(tile instanceof LavaTrap) {
				if((isKey((LavaTrap) tile)) && !foundKey(coordinate)){
					if(!collected(coordinate) && Path.hasPath(coordinate)) {
						
						goal.add(coordinate);
					//	System.out.println(goal);
					}
				} 
			}
		}
		//System.out.println(collected);
	}
	
	public boolean isKey(LavaTrap tile) {
		int keyNum = tile.getKey();
		if(keyNum !=0) {
			WorldSensor.addKey(keyNum);
			return true;
		}
		return false;
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
