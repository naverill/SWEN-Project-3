package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Move;
import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy extends BasicStrategy {
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = potentialPath(worldView);
		}
		//TODO handle types of tiles and acceleration/deceleration
		Coordinate nextMove = path.getNextMove();
		MapTile nextTile = WorldSensor.getTileAtCoordinate(nextMove);
		Move.Acceleration acceleration = adjustAcceleration(nextTile);
		
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(nextMove, acceleration);
	}

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
	
	public void reset(HashMap<Coordinate, MapTile> map) {
		path.clearPath();
		path = new Path(map, WorldSensor.getCurrentPosition(), goal);
	}

}
