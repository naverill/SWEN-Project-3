package mycontroller.util;

import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class Move {
	public static enum Acceleration { DECELERATE, NEUTRAL, BRAKE, ACCELERATE};
	public static enum RelativeDirection {LEFT, RIGHT, FORWARD, REVERSE, INVALID}
	public static final int MIN_FORWARD_SPEED = 1;
	public static final int MIN_REVERSE_SPEED = -1;

	private final Coordinate current;
	private final Coordinate target;
	private Acceleration acceleration;
	private final Direction direction;
	private final RelativeDirection relativeDirection;
	
    public Move(Coordinate target, Acceleration acceleration) {
    	this.current = WorldSensor.getCurrentPosition();
    	this.target = target;
    	this.acceleration = acceleration;
    	this.direction = WorldSensor.getOrientation();
    	this.relativeDirection = adjustRelativeDirection(current, target, direction);
    }
    
    public Move(Coordinate target) {
    	this.current = WorldSensor.getCurrentPosition();
    	this.target = target;
    	this.acceleration = adjustAcceleration();
    	this.direction = WorldSensor.getOrientation();
    	this.relativeDirection = adjustRelativeDirection(current, target, direction);
    }
    
	public RelativeDirection adjustRelativeDirection(Coordinate current, Coordinate target, Direction direction) {
		if(current.equals(target)) {
			return RelativeDirection.FORWARD;
		}
		float targetAngle = getAngle(current, target);
		
		float carAngle = WorldSpatial.rotation(direction);
		
		float delta = targetAngle - carAngle;
		
		//TODO verify maths here 
		if (delta < 0 ) delta += 360;
		
		if (Float.compare(delta, 0f)==0) {
			return RelativeDirection.FORWARD;
		} else if (Float.compare(delta, 90f)==0) {
			return RelativeDirection.LEFT;
		} else if (Float.compare(delta, 180f)==0) {
			return RelativeDirection.REVERSE;
		} else if (Float.compare(delta, 270f)==0) {
			return RelativeDirection.RIGHT;
		} else {
			return RelativeDirection.INVALID; // should never happen 
		}
	}
	
	public Move.Acceleration adjustAcceleration() {
		if(current.equals(target)) {
			return Acceleration.BRAKE;
		}
		
		MapTile nextTile = WorldSensor.getTileAtCoordinate(target);

		Coordinate currentPosition = WorldSensor.getCurrentPosition();
		
		//WorldSensor.isStart(WorldSensor.getTileAtCoordinate(currentPosition))
		if(WorldSensor.car.getSpeed() == 0.0f) {
			
			return Move.Acceleration.ACCELERATE;
		} else if(WorldSensor.isTrap(nextTile)) {
			return Move.Acceleration.ACCELERATE;
		} else if (WorldSensor.isHealth(nextTile)) {
			return Move.Acceleration.DECELERATE;
		} else if (WorldSensor.isStart(WorldSensor.getTileAtCoordinate(currentPosition))){
			return Move.Acceleration.ACCELERATE;
		} else {
			return Move.Acceleration.NEUTRAL;
		}
			
	}
	
	public float getAngle(Coordinate current, Coordinate target) {		
	    float delta = (float) Math.toDegrees(Math.atan2(target.y - current.y, target.x - current.x));
		
		if(delta < 0) delta += 360;

		return delta;
	}
	
	public Coordinate getCurrent() {
		return current;
	}

	public Coordinate getTarget() {
		return target;
	}

	public Acceleration getAcceleration() {
		return acceleration;
	}

	public Direction getDirection() {
		return direction;
	}

	public RelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

}
