package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class AIController extends CarController {
	
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 1;
	
	private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
	
	// Car Speed to move at
	private final int CAR_MAX_SPEED = 1;
	//TODO(naverill) remove this
	
	public AIController(Car car) {
		super(car);
	}
	
	// Coordinate initialGuess;
	// boolean notSouth = true;
	private HashMap<Coordinate, MapTile.Type> hey;
	private boolean flag = true;
	@Override
	public void update() {
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		HashMap<Coordinate,MapTile> currentMap = getMap();
		hey = new HashMap<>();
		if(flag) {
			for (Entry<Coordinate, MapTile>  entry : currentMap.entrySet()) {
				hey.put(entry.getKey(),entry.getValue().getType());
			}
			for (Entry<Coordinate, MapTile>  entry : currentMap.entrySet()) {
				hey.put(entry.getKey(),entry.getValue().getType());
			}System.out.println(hey);
			System.out.println(World.MAP_WIDTH);
			System.out.println(World.MAP_HEIGHT);
			flag = !flag;
		}
		
		getViewSpecifics(currentView);

		// checkStateChange();
		if(getSpeed() < CAR_MAX_SPEED){       // Need speed to turn and progress toward the exit
			applyForwardAcceleration();   // Tough luck if there's a wall in the way
		}
		if (isFollowingWall) {
			// If wall no longer on left, turn left
			if(!checkFollowingWall(getOrientation(), currentView)) {
				turnLeft();
			} else {
				// If wall on left and wall straight ahead, turn right
				if(checkWallAhead(getOrientation(), currentView)) {
					turnRight();
				}
			}
		} else {
			// Start wall-following (with wall on left) as soon as we see a wall straight ahead
			if(checkWallAhead(getOrientation(),currentView)) {
				turnRight();
				isFollowingWall = true;
			}
		}
	}
	
	public void getViewSpecifics(HashMap<Coordinate, MapTile> currentView){
		ArrayList<MapTile.Type> array = new ArrayList<>();
		int max = Car.VIEW_SQUARE;
		Coordinate currentPosition = new Coordinate(getPosition());
		//north
		for(int i = 1; i <= max; i++){
			MapTile tileNorth = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			MapTile.Type northType = tileNorth.getType();
			if(tileNorth instanceof LavaTrap) {
				if(((LavaTrap) tileNorth).getKey()!=0) {
					System.out.println("found key yo");
				}
			}
			array.add(northType);
		}
		//south
		for(int i = 1; i <= max; i++){
			MapTile tileSouth = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			MapTile.Type southType = tileSouth.getType();
			if(tileSouth instanceof LavaTrap) {
				if(((LavaTrap) tileSouth).getKey()!=0) {
					System.out.println("found key yo");
				}
			}
			array.add(southType);
		}
		//west
		for(int i = 1; i <= max; i++){
			MapTile tileWest = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			MapTile.Type westType = tileWest.getType();
			if(tileWest instanceof LavaTrap) {
				if(((LavaTrap) tileWest).getKey()!=0) {
					System.out.println("found key yo");
				}
			}
			array.add(westType);
		}
		//east
		for(int i = 1; i <= max; i++){
			MapTile tileEast= currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
			MapTile.Type eastType = tileEast.getType();
			if(tileEast instanceof LavaTrap) {
				if(((LavaTrap) tileEast).getKey()!=0) {
					System.out.println("found key yo");
				}
			}
			array.add(eastType);
		}
			
//			if(tile.isType(MapTile.Type.TRAP)){
//				if(tile instanceof LavaTrap) {
//					((LavaTrap) tile).getKey();
//					array.add(tile.getType());
//				}
//			}else {
//				array.add(tile.getType());
//			}
		System.out.println(array);
	}
	

	/**
	 * Check if you have a wall in front of you!
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
		switch(orientation){
		case EAST:
			return checkEast(currentView);
		case NORTH:
			return checkNorth(currentView);
		case SOUTH:
			return checkSouth(currentView);
		case WEST:
			return checkWest(currentView);
		default:
			return false;
		}
	}
	
	/**
	 * Check if the wall is on your left hand side given your orientation
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		
		switch(orientation){
		case EAST:
			return checkNorth(currentView);
		case NORTH:
			return checkWest(currentView);
		case SOUTH:
			return checkEast(currentView);
		case WEST:
			return checkSouth(currentView);
		default:
			return false;
		}	
	}
	
	/**
	 * Method below just iterates through the list and check in the correct coordinates.
	 * i.e. Given your current position is 10,10
	 * checkEast will check up to wallSensitivity amount of tiles to the right.
	 * checkWest will check up to wallSensitivity amount of tiles to the left.
	 * checkNorth will check up to wallSensitivity amount of tiles to the top.
	 * checkSouth will check up to wallSensitivity amount of tiles below.
	 */
	public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			if(tile.isType(MapTile.Type.WALL)){
				return true;
			}
		}
		return false;
	}
	
}
