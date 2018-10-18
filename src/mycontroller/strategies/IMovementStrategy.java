package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.Move;
import tiles.MapTile;
import utilities.Coordinate;

public interface IMovementStrategy {

	public Move move(HashMap<Coordinate, MapTile> hashMap);
	
	public void updateState(HashMap<Coordinate, MapTile> state);
	
	public void reset(HashMap<Coordinate, MapTile> map);

	public void applyBrake();

}

