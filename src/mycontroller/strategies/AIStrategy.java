package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mycontroller.Move;
import tiles.MapTile;
import utilities.Coordinate;

public class AIStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat,
	}
	
	public List<BasicStrategy>  strategies = new ArrayList<>();
	private BasicStrategy currentStrategy;

	public AIStrategy(HashMap<Coordinate, MapTile> map) {
	    strategies = (List<BasicStrategy>) Arrays.asList(
	    											   (BasicStrategy) new ExploreStrategy(map), 
	    											   (BasicStrategy) new HealthStrategy(), 
	    											   (BasicStrategy) new KeyStrategy()
	    											   );
		
		currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
	}
	
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(worldView);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(StrategyKey key : StrategyKey.values()) {
			strategies.get(key.ordinal()).updateState(state);
		}	
	}

	@Override
	public void reset(HashMap<Coordinate, MapTile> map) {
		currentStrategy.reset(map);
	}

	@Override
	public void applyBrake() {
		currentStrategy.applyBrake();
	}
	

}
