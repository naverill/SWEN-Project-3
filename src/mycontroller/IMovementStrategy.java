package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public interface IMovementStrategy {

	public Coordinate move(Coordinate currentPos, HashMap<Coordinate, MapTile> worldView);
}
