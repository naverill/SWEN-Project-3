package mycontroller.util;

import mycontroller.CarSensor;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

/**
 * This class provides functionality for 
 * identifying the optimal orientation, acceleration and relative orientation based on the next 
 * move suggested by a BasicStrategy
 * */
public class Move {
	public static enum Acceleration { DECELERATE, NEUTRAL, BRAKE, ACCELERATE}; // potential velocity changes 
	public static enum RelativeDirection {LEFT, RIGHT, FORWARD, REVERSE, INVALID} // relative directions to move
	public static final int MIN_FORWARD_SPEED = 1; //stable forward acceleration of car
	public static final int MIN_REVERSE_SPEED = -1; //stable negative acceleration of car

	private final Coordinate current; //current coordinate of the car
	private final Coordinate target; //target coordinate of next move
	private Acceleration acceleration; //optimal acceleration behaviour of the car
	private final Direction direction; //current orientation of the car
	private final RelativeDirection relativeDirection; //relative direction the car needs to orient itself
    
	/**
	 * This class provides functionality for identifying the optimal orientation, 
	 * acceleration and relative orientation based on the next move suggested 
	 * by a BasicStrategy
	 * @param target - the next optimal coordinate
	 * */
    public Move(Coordinate target) {
    	this.current = CarSensor.getCurrentPosition();
    	this.target = target;
    	this.acceleration = adjustAcceleration();
    	this.direction = CarSensor.getOrientation();
    	this.relativeDirection = adjustRelativeDirection(current, target, direction);
    }
    
	/**
	 * Method takes in a parameter of the car's current location, target location and current 
	 * direction and returns the relative direction the car needs to turn (forward, backward,
	 *  left, right)
	 * @param current - current location of the car
	 * @param target - target location of the car
	 * @param direction - current orientation of the car
	 * */
	public RelativeDirection adjustRelativeDirection(Coordinate current, Coordinate target, Direction direction) {
		//if car is currently located on optimal coordinate, keep same orientation
		if(current.equals(target)) {
			return RelativeDirection.FORWARD;
		}
		
		//angle representing cardinal direction of target
		float targetAngle = getAngle(current, target);  
		
		//angle representing current orientation of car 
		float carAngle = WorldSpatial.rotation(direction); 
		
		float delta = targetAngle - carAngle; 
		
		if (delta < 0 ) delta += 360; // scale to range [0, 360]
		
		if (Float.compare(delta, 0f)==0) {
			return RelativeDirection.FORWARD;
		} else if (Float.compare(delta, 90f)==0) {
			return RelativeDirection.LEFT;
		} else if (Float.compare(delta, 180f)==0) {
			return RelativeDirection.REVERSE;
		} else if (Float.compare(delta, 270f)==0) {
			return RelativeDirection.RIGHT;
		} else {
			// should never happen 
			//indicates next tile is not in cardinal direction
			return RelativeDirection.INVALID; 
		}
	}
	
	/**
	 * Method determines the optimal acceleration of the car based on the tile type 
	 * at the next coordinate
	 * */
	public Move.Acceleration adjustAcceleration() {
		//if car is currently located on optimal coordinate, apply brakes
		if(current.equals(target)) {
			return Acceleration.BRAKE;
		}
		
		MapTile nextTile = CarSensor.getTileAtCoordinate(target);

		Coordinate currentPosition = CarSensor.getCurrentPosition();
		
		//adjust velocity based on current/future tile conditions
		if(CarSensor.getVelocity() == 0.0f) {
			return Move.Acceleration.ACCELERATE;
		} else if(CarSensor.isTrap(nextTile)) {
			//TODO changed to neutral from accelarate
			return Move.Acceleration.NEUTRAL;
		} else if (CarSensor.isHealth(nextTile)) {
			return Move.Acceleration.DECELERATE;
		} else if (CarSensor.isStart(CarSensor.getTileAtCoordinate(currentPosition))){
			return Move.Acceleration.ACCELERATE;
		} else {
			return Move.Acceleration.NEUTRAL;
		}
			
	}
	
	/**
	 * Method finds the angle representing dirction of  future tile.
	 * should return one of:
	 *  0˚ - tile is east of current 
	 *  90˚ - tile is north of current
	 *  180˚ - tile is west of current
	 *  270˚ - tile is south of current 
	 *  @param current - current location of car
	 *  @param target - target location of car
	 * */
	public float getAngle(Coordinate current, Coordinate target) {		
		//angle between points
	    float delta = (float) Math.toDegrees(Math.atan2(target.y - current.y, target.x - current.x));
		if(delta < 0) delta += 360; // scale to range [0, 360]
		return delta;
	}
	
	/**
	 * Returns location of car at instantiation of the move
	 * */
	public Coordinate getCurrent() {
		return current;
	}

	/**
	 * Returns next optimal coordinate of the car at instantiation of the move
	 * */
	public Coordinate getTarget() {
		return target;
	}

	/**
	 * Returns next optimal acceleration of the car based on conditions at instantiation
	 * of the move (next move, type of tile at next move)
	 * */
	public Acceleration getAcceleration() {
		return acceleration;
	}

	/**
	 * Returns current orientation of car at instantiation of the move
	 * */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Returns relative direction the car needs to turn to reach the target tile
	 * */
	public RelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

}
