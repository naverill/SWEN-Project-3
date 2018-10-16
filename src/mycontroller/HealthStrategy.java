package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public class HealthStrategy implements IMovementStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();
	Path healthPath;

	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile> worldView) {
		if(healthPath==null) {
			healthPath = new Path(currentPos, health, worldView);
		}
		if(healthPath.isEmpty()) {
			healthPath = new Path(currentPos, health, worldView);
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
