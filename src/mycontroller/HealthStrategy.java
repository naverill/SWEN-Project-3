package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy implements IMovementStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();
	Path healthPath = new Path();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		if(healthPath.endPath()) {
			healthPath = new Path(WorldSensor.getCurrentPosition(), health, worldView);
		} 
		
		return healthPath.getNextMove();
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
