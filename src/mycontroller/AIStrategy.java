package mycontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;
import world.WorldSpatial.Direction;


public class AIStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat,
	}
	
	public List<IMovementStrategy>  strategies = new ArrayList<>();
	private IMovementStrategy currentStrategy;

	public AIStrategy(HashMap<Coordinate, MapTile> map) {
	    strategies = (List<IMovementStrategy>) Arrays.asList(
	    											   (IMovementStrategy) new ExploreStrategy(map), 
	    											   (IMovementStrategy) new HealthStrategy(), 
	    											   (IMovementStrategy) new KeyStrategy()
	    											   );
		
		currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
	}
	
	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(direction, currentPos, worldView);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(StrategyKey key : StrategyKey.values()) {
			strategies.get(key.ordinal()).updateState(state);
		}	
	}
	

}
