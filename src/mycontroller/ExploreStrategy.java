package mycontroller;

import java.util.HashMap;

import mycontroller.MyAIController.RelativeDirection;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class ExploreStrategy implements IMovementStrategy {
	private HashMap<Coordinate, Boolean> explored;
	
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		for(Coordinate key : map.keySet()) {
			explored.put(key, false);
		}
	}

	@Override
	public Coordinate move(Coordinate currentPos, HashMap<Coordinate, MapTile> worldView) {		
		if (isFollowingWall) {
			// If wall no longer on left, turn left
			if(!checkFollowingWall(currentPos, worldView)) {
				return directionDelta(changeDirection(RelativeDirection.LEFT, ));
			} else {
				// If wall on left and wall straight ahead, turn right
				if(checkWallAhead(getOrientation(), worldView)) {
					turnRight();
				}
			}
		} else {
			// Start wall-following (with wall on left) as soon as we see a wall straight ahead
			if(checkWallAhead(getOrientation(),worldView)) {
				turnRight();
				isFollowingWall = true;
			}
		}	}
	
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
		@Override
		public void update() {
			// Gets what the car can see
		}

		/**
		 * Check if you have a wall in front of you!
		 * @param orientation the orientation we are in based on WorldSpatial
		 * @param currentView what the car can currently see
		 * @return
		 */
		private boolean checkWallAhead(Coordinate currentPosition, WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
			switch(orientation){
			case EAST:
				return checkEast(currentPosition, currentView);
			case NORTH:
				return checkNorth(currentPosition, currentView);
			case SOUTH:
				return checkSouth(currentPosition, currentView);
			case WEST:
				return checkWest(currentPosition, currentView);
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
		private boolean checkFollowingWall(Coordinate currentPosition, WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
			
			switch(orientation){
			case EAST:
				return checkNorth(currentPosition, currentView);
			case NORTH:
				return checkWest(currentPosition, currentView);
			case SOUTH:
				return checkEast(currentPosition, currentView);
			case WEST:
				return checkSouth(currentPosition, currentView);
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
		public boolean checkEast(Coordinate currentPosition, HashMap<Coordinate, MapTile> currentView){
			// Check tiles to my right
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkWest(Coordinate currentPosition, HashMap<Coordinate,MapTile> currentView){
			// Check tiles to my left
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkNorth(Coordinate currentPosition, HashMap<Coordinate,MapTile> currentView){
			// Check tiles to towards the top
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkSouth(Coordinate currentPosition, HashMap<Coordinate,MapTile> currentView){
			// Check tiles towards the bottom
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public Coordinate directionDelta(Direction d) {
			switch (d) {
			case NORTH:
				return new Coordinate( 0,  1);
			case EAST:
				return new Coordinate( 1,  0);
			case SOUTH:
				return new Coordinate( 0, -1);
			case WEST:
				return new Coordinate(-1,  0);
			default:
				return new Coordinate( 0,  0); // Should never happen
			}
		}

}
