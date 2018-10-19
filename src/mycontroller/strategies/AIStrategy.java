package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import mycontroller.CarSensor;
import mycontroller.util.Move;
import tiles.MapTile;
import utilities.Coordinate;

/**
* Custom AI Strategy class that encompasses and handles the logic of traversal strategies 
*/
public class AIStrategy implements IMovementStrategy {
	private static final int EXPLORE = 0; //index of explore strategy (using an enum inhibited readability of the code)
	private static final int HEALTH = 1; //index of health strategy
	private static final int KEY = 2; //index of key strategy
	private static final int FINISH = 3; //index of finish strategy
	private static final int NUM_STRATEGIES = 4; //number of strategies used 
	
	public List<BasicStrategy>  strategies = new ArrayList<>(); //list of basic strategies the AI employs
	private BasicStrategy currentStrategy; //the current strategy


	/**
	* The AI Strategy object that controls the movement of the car
	* @param map -  the map of the world at initialisation
	*/
	public AIStrategy(HashMap<Coordinate, MapTile> map) {
		strategies = (List<BasicStrategy>) Arrays.asList(
	    											   (BasicStrategy) new ExploreStrategy(map), 
	    											   (BasicStrategy) new HealthStrategy(), 
	    											   (BasicStrategy) new KeyStrategy(),
	    											   (BasicStrategy) new FinishStrategy()
	    											   );
	}

	/**
	* Generate a movement based on the current strategy being employed
	* @param map -  the world map of all tiles 
	*/
	@Override
	public Move move(HashMap<Coordinate, MapTile> map) {		
		return currentStrategy.move(map);
	}

	/**
	* Read in current state of the and updating the state of the traversal strategies 
	* @param view -  the current view of the car
	*/
	@Override
	public void updateState(HashMap<Coordinate, MapTile> view) {
		for(int i=0; i< NUM_STRATEGIES; i++) {
			strategies.get(i).updateState(view);
		}
		//switch strategies based on current state 
		determineState(); 
	}

	/**
	* Reset the current strategy
	* @param map -  the world map of all tiles 
	*/
	@Override
	public void reset(HashMap<Coordinate, MapTile> map) {
		currentStrategy.reset(map);
	}

	/**
	* Switch current strategy according to the current state of the World and car
	* @param map -  the world map of all tiles 
	*/
	public void determineState() {
		//don't switch if car is healing 
		if(currentlyHealing() && !fullyHealed()) {
			return;
		}
		
		//health tile is located and car is at critical health
		if(foundHealth() && nearCriticalHealth()) {
			currentStrategy = strategies.get(HEALTH);
			return;
		}

		//All tiles have been explored
		if(hasExploredEverything()) {
			
			if(CarSensor.hasAllKeys()) {
				//attempt to reach finish Tile
				tryToFinish();
				
			} else {
				tryToFindKeys();
			}
			return;
		}
		//default to exploring 
		currentStrategy = strategies.get(EXPLORE);
	}
	
	/**
	* Evaluates whether finishing tile can be reached based on current health 
	*  and most efficient path 
	*/
	private void tryToFinish() {
		//get most efficient path to finish tile 
		Stack<Coordinate> pathToFinish = strategies.get(FINISH).potentialPath(
				CarSensor.getWorldMap()).getCurrentPath();
		
		//find health if insufficient 
		if (CarSensor.nearCriticalLowHealth(pathToFinish)) {
			currentStrategy = strategies.get(HEALTH);
		} else {
			currentStrategy=strategies.get(FINISH);
		}
	}

	/**
	* Evaluates whether keys tile can be collected based on current health 
	*  and most efficient path 
	*/
	private void tryToFindKeys() {
		Stack<Coordinate> pathToKey = strategies.get(KEY).potentialPath(
				CarSensor.getWorldMap()).getCurrentPath();
		
		if(CarSensor.hasEnoughHealth(pathToKey)) {
			currentStrategy = strategies.get(KEY);
		} 
		else if (CarSensor.nearCriticalLowHealth(pathToKey)) {
			currentStrategy = strategies.get(HEALTH);
		}
	}
	
	/**
	* Returns whether car is currently located on a health tile and is employing the healing strategy
	*/
	public boolean currentlyHealing() {
		return CarSensor.isHealing() && currentStrategy==strategies.get(HEALTH); //and current strategy is health
	}
	
	/**
	* Returns whether car is at full health
	*/
	public boolean fullyHealed() {
		return CarSensor.isDoneHealing();
	}

	/**
	* Returns whether car has explored all areas of the map
	*/
	//returns true if it has explored everything
	public boolean hasExploredEverything(){
		return strategies.get(EXPLORE).goal.isEmpty();
	}
	
	/**
	* Returns whether car has identified the location of any keys
	*/
	public boolean keysCollected(){
		return strategies.get(KEY).goal.isEmpty();
	}
	
	/**
	* Returns whether car has identified the location of any health tiles
	*/
	public boolean foundHealth(){
		return strategies.get(HEALTH).foundGoalTile();
	}
	
	/**
	* Identifies whether car has enough health to traverse the current path 
	*/
	public boolean nearCriticalHealth() {
		Stack<Coordinate> pathToHeal = strategies.get(HEALTH).potentialPath(CarSensor.getWorldMap()).getCurrentPath();
		
		return CarSensor.nearCriticalLowHealth(pathToHeal);
	}

}
