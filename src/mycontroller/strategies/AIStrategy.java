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
	//TODO dont pass sensor cause then passing map would be redundant
	public AIStrategy(HashMap<Coordinate, MapTile> map) {
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
<<<<<<< Updated upstream
			if(sensor.hasAllKeys()) {
<<<<<<< Updated upstream
=======
				System.out.println("i have keys");
=======
			if(WorldSensor.hasAllKeys()) {
>>>>>>> Stashed changes
>>>>>>> Stashed changes
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
				WorldSensor.getWorldMap()).getPath();
		
<<<<<<< Updated upstream
		if(sensor.hasEnoughHealth(pathToFinish)) {
=======
<<<<<<< Updated upstream
		if (sensor.nearCriticalLowHealth(pathToFinish)) {
			System.out.println("need health");
=======
		if(WorldSensor.hasEnoughHealth(pathToFinish)) {
			currentStrategy=strategies.get(FINISH);
		} else {
>>>>>>> Stashed changes
			currentStrategy = strategies.get(HEALTH);
		} else {
>>>>>>> Stashed changes
			currentStrategy=strategies.get(FINISH);
		} else {
			currentStrategy = strategies.get(HEALTH);
		}
	}

	private void tryToFindKeys() {
		Stack<Coordinate> pathToKey = strategies.get(KEY).potentialPath(
				WorldSensor.getWorldMap()).getPath();
		
		if(WorldSensor.hasEnoughHealth(pathToKey)) {
			currentStrategy = strategies.get(KEY);
		} else {
			currentStrategy = strategies.get(HEALTH);
		}
	}
	
	public boolean currentlyHealing() {
		return WorldSensor.isHealing() && currentStrategy==strategies.get(HEALTH); 
	}
	
	public boolean fullyHealed() {
		return WorldSensor.isDoneHealing();
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
		Stack<Coordinate> pathToHeal = strategies.get(HEALTH).potentialPath(WorldSensor.getWorldMap()).getPath();
		
		return WorldSensor.nearCriticalLowHealth(pathToHeal);
	}

}
