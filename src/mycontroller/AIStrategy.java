package mycontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.Direction;


public class AIStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat,
		kPathFollowStrat
	}
	
	public List<IMovementStrategy>  strategies = new ArrayList<>();
	private IMovementStrategy currentStrategy;

	public AIStrategy() {
	    strategies = (List<IMovementStrategy>) Arrays.asList(
	    											   (IMovementStrategy) new ExploreStrategy(null), 
	    											   (IMovementStrategy) new HealthStrategy(), 
	    											   (IMovementStrategy) new KeyStrategy()
	    											   );
		
		currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
	}
	
	@Override
	public Coordinate move(Direction direction, Coordinate currentPos, HashMap<Coordinate, MapTile.Type> worldView) {		
		return currentStrategy.move(direction, currentPos, worldView);
	}
	

}
