package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Move;
import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public abstract class BasicStrategy implements IMovementStrategy{
	protected Path path = new Path();
	protected ArrayList<Coordinate> goal = new ArrayList<>();


	@Override
	public abstract Move move(HashMap<Coordinate, MapTile> hashMap);

	@Override
	public abstract void updateState(HashMap<Coordinate, MapTile> state);
	
	public boolean foundGoalTile() {
		return !goal.isEmpty();
	}
	
	public void reset(HashMap<Coordinate, MapTile> map) {
		path.clearPath();
		path = new Path(map, WorldSensor.getCurrentPosition(), goal);
	}
	
	public void applyBrake() {
		path.addPathNode(WorldSensor.getCurrentPosition());
	}

}
