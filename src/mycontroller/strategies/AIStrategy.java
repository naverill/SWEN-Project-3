package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import mycontroller.Move;
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
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(worldView);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(StrategyKey key : StrategyKey.values()) {
			strategies.get(key.ordinal()).updateState(state);
		}	
		determineState();
	}

	@Override
	public void reset(HashMap<Coordinate, MapTile> map) {
		currentStrategy.reset(map);
	}

	@Override
	public void applyBrake() {
		currentStrategy.applyBrake();
	}
	
	public void determineState() {
		//need to heal (can be null)
		if(!strategies.get(StrategyKey.kKeyStrat.ordinal()).goal.isEmpty()) {
			//have healthTile as goal, we can simulate movement
			Stack<Coordinate> pathToHeal = strategies.get(StrategyKey.kHealthStrat.ordinal()).potentialPath(
					sensor.getWorldMap()).pathCoordinates;
			if(!sensor.dangerZone(pathToHeal)) {
				currentStrategy = strategies.get(StrategyKey.kHealthStrat.ordinal());
				System.out.println(currentStrategy.getClass());
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
	}
	
	private void tryToFinish() {
		//already doing finish strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kFinishStrat.ordinal()))){
			//possible bug, might need to heal along the way
			Stack<Coordinate> pathToFinish = strategies.get(StrategyKey.kFinishStrat.ordinal()).potentialPath(
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
		System.out.println(currentStrategy.getClass());
	}

	private void tryToFindKeys() {
		//already doing key strategy?
		if(!currentStrategy.equals(strategies.get(StrategyKey.kKeyStrat.ordinal()))){
			//simulate movement (can be null)
			Stack<Coordinate> pathToKey = strategies.get(StrategyKey.kKeyStrat.ordinal()).potentialPath(
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
		System.out.println(currentStrategy.getClass());
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
		System.out.println(currentStrategy.getClass());
	}


	//returns true if it has explored everything
	public boolean hasExploredEverything(){
		return strategies.get(StrategyKey.kExploreStrat.ordinal()).goal.isEmpty();
	}
	

}
