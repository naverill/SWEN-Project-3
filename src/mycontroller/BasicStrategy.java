package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public abstract class BasicStrategy implements IMovementStrategy{
	Path path = new Path();

	@Override
	public abstract Coordinate move(HashMap<Coordinate, MapTile> hashMap);

	@Override
	public abstract void updateState(HashMap<Coordinate, MapTile> state);
	
	public void reset() {
		path.clearPath();
	}

}
