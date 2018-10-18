package mycontroller.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import mycontroller.WorldSensor;
import mycontroller.util.Move;
import mycontroller.util.Path;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealthStrategy extends BasicStrategy {
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = potentialPath(worldView);
		}
				
		if(currentlyHealing() && !fullyHealed()) {
			System.out.println("BREAK");
			return new Move(WorldSensor.getCurrentPosition());
		}
		
		Coordinate nextMove = path.getNextMove();
		return new Move(nextMove);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(state.get(coordinate) instanceof HealthTrap) {
				if(!goal.contains(coordinate)) {
					goal.add(coordinate);
				}
			}
		}
	}
	
	public void reset(HashMap<Coordinate, MapTile> map) {
		path.clearPath();
		path = new Path(map, WorldSensor.getCurrentPosition(), goal);
	}
	
	public boolean currentlyHealing() {
		return WorldSensor.isHealing();
	}
	
	public boolean fullyHealed() {
		return WorldSensor.isDoneHealing();
	}

}
