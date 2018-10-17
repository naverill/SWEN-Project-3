package mycontroller.strategies;

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
			path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
		}
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(path.getNextMove(), Move.Acceleration.ACCELERATE);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate) instanceof HealthTrap) {
				goal.add(coordinate);
			}
		}
	}

}
