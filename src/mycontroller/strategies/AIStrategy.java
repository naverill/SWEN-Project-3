package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public class AIStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat,
		kFinishStrat
	}
	
	public List<BasicStrategy>  strategies = new ArrayList<>();
	private BasicStrategy currentStrategy;
	private WorldSensor sensor;
	//TODO dont pass sensor cause then passing map would be redundant
	public AIStrategy(HashMap<Coordinate, MapTile> map, WorldSensor sensor) {
	    this.sensor = sensor;
		strategies = (List<BasicStrategy>) Arrays.asList(
	    											   (BasicStrategy) new ExploreStrategy(map), 
	    											   (BasicStrategy) new HealthStrategy(), 
	    											   (BasicStrategy) new KeyStrategy(),
	    											   (BasicStrategy) new FinishStrategy()
	    											   );
		currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
	}

	
	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(worldView);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(StrategyKey key : StrategyKey.values()) {
			strategies.get(key.ordinal()).updateState(state);
		}	
		determineState();
	}
	
	public void determineState() {
		//need to heal (can be null)
		Stack<Coordinate> pathToHeal = strategies.get(StrategyKey.kKeyStrat.ordinal()).simulateMovement(
				sensor.getWorldMap()).pathCoordinates;
		if(!sensor.hasEnoughHealth(pathToHeal)) {
			currentStrategy = strategies.get(StrategyKey.kHealthStrat.ordinal());
		}
		//for now assume it has all the health in the world to explore
		else if(!hasExploredEverything()) {
			currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
		}
		//has all keys? try to finish
		else if(sensor.hasAllKeys()) {
			tryToFinish();
		}
		//if there are keys to find
		else if(!strategies.get(StrategyKey.kKeyStrat.ordinal()).goal.isEmpty()) {
			tryToFindKeys();
		}
		else {
			//probably healing
			currentlyHealing();
		}	
	}
	
	private void tryToFinish() {
		//already doing finish strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kFinishStrat.ordinal()))){
			//TODO magic number if car full health
			if(sensor.car.getHealth()==100) {
				currentStrategy=strategies.get(StrategyKey.kFinishStrat.ordinal());
			}else {
				currentStrategy=strategies.get(StrategyKey.kFinishStrat.ordinal());
			}
		}
	}

	private void tryToFindKeys() {
		//already doing key strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kKeyStrat.ordinal()))){
			//simulate movement (can be null)
			Stack<Coordinate> stack = strategies.get(StrategyKey.kKeyStrat.ordinal()).simulateMovement(
					sensor.getWorldMap()).pathCoordinates;
			if(sensor.hasEnoughHealth(stack)) {
				currentStrategy = strategies.get(StrategyKey.kKeyStrat.ordinal());
			}else {
				currentStrategy = strategies.get(StrategyKey.kHealthStrat.ordinal());
			}
		}
	}
	
	private void currentlyHealing() {
		//if none of the above apply, it's probably healing
		if(currentStrategy.equals(strategies.get(StrategyKey.kHealthStrat.ordinal()))){
			if(sensor.isHealing()) {
				//if it's done healing and has explored everything resume keyfinding
				if(sensor.isDoneHealing()){
					if(hasExploredEverything()) {
						currentStrategy = strategies.get(StrategyKey.kKeyStrat.ordinal());
					}else {
						currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
					}
				}
				//still healing
				return;
			}
		}
	}


	//returns true if it has explored everything
	public boolean hasExploredEverything(){
		return strategies.get(StrategyKey.kExploreStrat.ordinal()).goal.isEmpty();
	}
	

}
