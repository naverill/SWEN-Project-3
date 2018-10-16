package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy implements IMovementStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();
	Path healthPath;

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		if(healthPath==null) {
			healthPath = new Path(null, health, worldView);
		}
		if(healthPath.isEmpty()) {
			healthPath = new Path(null, health, worldView);
		} 
		
		return healthPath.getNextMove();
	}
	
	public boolean foundHealth() {
		return !health.isEmpty();
	}

}
