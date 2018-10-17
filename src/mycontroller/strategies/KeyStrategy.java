package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy extends BasicStrategy {
	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {
		if(path.endPath()) {
			path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
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
