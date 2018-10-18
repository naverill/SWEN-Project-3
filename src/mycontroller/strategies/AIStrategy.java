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
		if(!strategies.get(StrategyKey.kKeyStrat.ordinal()).goal.isEmpty()) {
			//have healthTile as goal, we can simulate movement
			Stack<Coordinate> pathToHeal = strategies.get(StrategyKey.kHealthStrat.ordinal()).simulateMovement(
					sensor.getWorldMap()).pathCoordinates;
			if(!sensor.dangerZone(pathToHeal)) {
				currentStrategy = strategies.get(StrategyKey.kHealthStrat.ordinal());
				return;
			}
		}
		
		//means we have enough health to do other things
		if(!hasExploredEverything()) {
			currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
		}
		//has all keys? try to finish
		else if(sensor.hasAllKeys()) {
			tryToFinish();
		}
		//if there are keys to find (might be redundant, since if you have explored everything it must mean you have all the keys.)
		else if(!strategies.get(StrategyKey.kKeyStrat.ordinal()).goal.isEmpty()) {
			tryToFindKeys();
		}
		System.out.println("something's wrong in determining the state dawg");
	}
	
	private void tryToFinish() {
		//already doing finish strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kFinishStrat.ordinal()))){
			//possible bug, might need to heal along the way
			Stack<Coordinate> pathToFinish = strategies.get(StrategyKey.kFinishStrat.ordinal()).simulateMovement(
					sensor.getWorldMap()).pathCoordinates;
			//path to finish shouldnt be null since weve explored everything
			if(sensor.hasEnoughHealth(pathToFinish)) {
				currentStrategy=strategies.get(StrategyKey.kFinishStrat.ordinal());
			}else {
				currentStrategy=strategies.get(StrategyKey.kHealthStrat.ordinal());
			}
		}
		else if(currentStrategy.equals(strategies.get(StrategyKey.kHealthStrat.ordinal()))) {
			currentlyHealing();
		}
		System.out.println("im trying to finish dont rush me fool");
	}

	private void tryToFindKeys() {
		//already doing key strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kKeyStrat.ordinal()))){
			//simulate movement (can be null)
			Stack<Coordinate> pathToKey = strategies.get(StrategyKey.kKeyStrat.ordinal()).simulateMovement(
					sensor.getWorldMap()).pathCoordinates;
			//path to key shouldnt be null since weve explored everything
			if(sensor.hasEnoughHealth(pathToKey)) {
				currentStrategy = strategies.get(StrategyKey.kKeyStrat.ordinal());
			}else {
				currentStrategy = strategies.get(StrategyKey.kHealthStrat.ordinal());
			}
		}else if(currentStrategy.equals(strategies.get(StrategyKey.kHealthStrat.ordinal()))) {
			currentlyHealing();
		}
		System.out.println("im otw to keys dont rush me fool");
	}
	
	private void currentlyHealing() {
		if(sensor.isHealing()) {
			//if it's done healing and has explored everything resume keyfinding
			if(sensor.isDoneHealing()){
				if(hasExploredEverything()) {
					determineState();
					System.out.println("pls no infinite loop");
				}else {
					currentStrategy = strategies.get(StrategyKey.kExploreStrat.ordinal());
					System.out.println("for some reason it hasnt explored everything, something's wrong with the dangerZone check");
				}
			}
			//still healing
			System.out.println("im HEELING dont rush me fool");
			return;
		}
		System.out.println("im otw to HEEL dont rush me fool");
	}


	//returns true if it has explored everything
	public boolean hasExploredEverything(){
		return strategies.get(StrategyKey.kExploreStrat.ordinal()).goal.isEmpty();
	}
	

}
