package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;

public interface IMovementStrategy {

	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile> hashMap);
	
	public void updateState(HashMap<Coordinate, MapTile> state);
}

