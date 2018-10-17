package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy extends BasicStrategy {
	ArrayList<Coordinate> collected = new ArrayList<>();

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		Coordinate keyTile = path.getNextMove();
		
		if(path.endPath()) {
			collected.add(keyTile);
			path = new Path(WorldSensor.getCurrentPosition(), goal, worldView);
		} 
		
		return path.getNextMove();
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			MapTile tile = state.get(coordinate);
			if(tile instanceof LavaTrap) {
				int possibleKey = ((LavaTrap) tile).getKey();
				if((possibleKey!=0)&&!goal.contains(coordinate)){
					goal.add(coordinate);
				} 
			}
		}
	}
}
