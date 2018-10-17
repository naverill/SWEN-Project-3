package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy extends BasicStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		Coordinate healthTile = path.getNextMove();
		
		if(path.endPath()) {
			path = new Path(WorldSensor.getCurrentPosition(), health, worldView);
		}
		
		return healthTile;
	}
	
	public boolean foundHealth() {
		return !health.isEmpty();
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate) instanceof HealthTrap) {
				health.add(coordinate);
			}
		}
	}

}
