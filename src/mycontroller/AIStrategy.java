package mycontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;


public class AIStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat,
		kPathFollowStrat
	}
	
	public ArrayList<IMovementStrategy>  strategies = new ArrayList<>();
	private IMovementStrategy currentStrategy;

	public AIStrategy() {
	    strategies = (ArrayList<IMovementStrategy>) Arrays.asList(
	    											   (IMovementStrategy) new ExploreStrategy(null), 
	    											   (IMovementStrategy) new HealthStrategy(), 
	    											   (IMovementStrategy) new KeyStrategy()
	    											   );
		
		currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
	}
	
	@Override
	public Coordinate move(Coordinate currentPos, HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(currentPos, worldView);
	}
	

}
