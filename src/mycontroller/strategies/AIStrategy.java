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
	private static final int EXPLORE = 0;
	private static final int HEALTH = 1;
	private static final int KEY = 2;
	private static final int FINISH = 3;
	private static final int NUM_STRATEGIES = 4;
	
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
		currentStrategy = strategies.get(EXPLORE);
	}

	
	@Override
	public Move move(HashMap<Coordinate, MapTile> worldView) {		
		return currentStrategy.move(worldView);
	}

	@Override
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(int i=0; i< NUM_STRATEGIES; i++) {
			strategies.get(i).updateState(state);
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
		if(currentlyHealing() && !fullyHealed()) {
			System.out.println(currentStrategy.getClass());
			return;
		}
		
		if(foundHealth() && nearCriticalHealth()) {
			currentStrategy = strategies.get(HEALTH);
			System.out.println(currentStrategy.getClass());
			return;
		}

		//means we have enough health to do other things
		if(hasExploredEverything()) {
			if(sensor.hasAllKeys()) {
				tryToFinish();
			} else {
				tryToFindKeys();
			}
			System.out.println(currentStrategy.getClass());
			return;
		}

		currentStrategy = strategies.get(EXPLORE);
		System.out.println(currentStrategy.getClass());
	}
	
	private void tryToFinish() {
		Stack<Coordinate> pathToFinish = strategies.get(FINISH).potentialPath(
				sensor.getWorldMap()).getPath();
		
		if(sensor.hasEnoughHealth(pathToFinish)) {
			currentStrategy=strategies.get(FINISH);
		} else {
			currentStrategy = strategies.get(HEALTH);
		}
	}

	private void tryToFindKeys() {
		Stack<Coordinate> pathToKey = strategies.get(KEY).potentialPath(
				sensor.getWorldMap()).getPath();
		
		if(sensor.hasEnoughHealth(pathToKey)) {
			currentStrategy = strategies.get(KEY);
		} else {
			currentStrategy = strategies.get(HEALTH);
		}
	}
	
	public boolean currentlyHealing() {
		return sensor.isHealing() && currentStrategy==strategies.get(HEALTH); //and current strategy is health
	}
	
	public boolean fullyHealed() {
		return sensor.isDoneHealing();
	}

	//returns true if it has explored everything
	public boolean hasExploredEverything(){
		return strategies.get(EXPLORE).goal.isEmpty();
	}
	
	public boolean keysCollected(){
		return strategies.get(KEY).goal.isEmpty();
	}
	
	public boolean foundHealth(){
		return strategies.get(HEALTH).foundGoalTile();
	}
	
	public boolean nearCriticalHealth() {
		Stack<Coordinate> pathToHeal = strategies.get(HEALTH).potentialPath(sensor.getWorldMap()).getPath();
		
		return sensor.nearCriticalLowHealth(pathToHeal);
	}

}
