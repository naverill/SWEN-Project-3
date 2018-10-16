package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public class HealthStrategy implements IMovementStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();
	Path healthPath;

	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile.Type> worldView) {
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

}
