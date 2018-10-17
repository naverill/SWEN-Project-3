package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public abstract class BasicStrategy implements IMovementStrategy{
	protected Path path = new Path();
	protected ArrayList<Coordinate> goal = new ArrayList<>();


	@Override
	public abstract Coordinate move(HashMap<Coordinate, MapTile> hashMap);

	@Override
	public abstract void updateState(HashMap<Coordinate, MapTile> state);


	public void reset() {
		path.clearPath();
	}
	
	public boolean foundGoalTile() {
		return !goal.isEmpty();
	}
	
	public Path simulateMovement(HashMap<Coordinate, MapTile> worldView) {
		return new Path(worldView, WorldSensor.getCurrentPosition(), goal);
	}

}
