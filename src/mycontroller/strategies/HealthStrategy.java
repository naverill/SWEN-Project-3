package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy extends BasicStrategy {
	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
		}
		
		return 	path.getNextMove();
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
