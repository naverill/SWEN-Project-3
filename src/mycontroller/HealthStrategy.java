package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy implements IMovementStrategy {
	ArrayList<Coordinate> health = new ArrayList<>();
	Stack<Coordinate> healthPath = new Stack<>();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		if(healthPath.isEmpty()) {
			healthPath = new Path();
		} 
		
		return healthPath.pop();
	}
	
	public boolean foundHealth() {
		return !health.isEmpty();
	}

}
