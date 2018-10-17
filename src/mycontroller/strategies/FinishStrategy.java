package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.HealthTrap;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class FinishStrategy extends BasicStrategy{

	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
		}
		
		return 	path.getNextMove();
	}

	
	//TODO make world sensor static so that we can access the finish line easily
	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate).getType().equals(Type.FINISH)) {
				if(!goal.contains(coordinate)) {
					goal.add(coordinate);
				}
			}
		}
	}
	
}
