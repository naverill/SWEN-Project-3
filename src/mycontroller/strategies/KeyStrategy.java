package mycontroller.strategies;

import java.util.HashMap;
import mycontroller.Move;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyStrategy extends BasicStrategy {
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {
		if(path.endPath()) {
			path = potentialPath(worldView);
		} 
		//TODO handle types of tiles and acceleration/deceleration
		return new Move(path.getNextMove(), Move.Acceleration.ACCELERATE);
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
