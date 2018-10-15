package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public interface IMovementStrategy {

	public void move(HashMap<Coordinate, MapTile> worldView);
}
