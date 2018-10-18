package mycontroller.strategies;

import java.util.HashMap;

import mycontroller.Move;
import mycontroller.WorldSensor;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class FinishStrategy extends BasicStrategy{

	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			path = potentialPath(worldView);
		}
		
		Coordinate nextMove = path.getNextMove();
		MapTile nextTile = WorldSensor.getTileAtCoordinate(nextMove);
		Move.Acceleration acceleration = adjustAcceleration(nextTile);
		
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(nextMove, acceleration);	
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
