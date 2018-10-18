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
	
	public Move.Acceleration adjustAcceleration(MapTile nextTile) {
		Coordinate currentPosition = WorldSensor.getCurrentPosition();
		
		if(WorldSensor.isStart(WorldSensor.getTileAtCoordinate(currentPosition))) {
			return Move.Acceleration.ACCELERATE;
		} else if(WorldSensor.isTrap(nextTile)) {
			return Move.Acceleration.ACCELERATE;
		} else if (WorldSensor.isHealth(nextTile)) {
			return Move.Acceleration.DECELERATE;
		} else {
			return Move.Acceleration.NEUTRAL;
		}
	}
	
	public Path potentialPath(HashMap<Coordinate, MapTile> worldView) {
		return new Path(worldView, WorldSensor.getCurrentPosition(), goal);
	}

}
