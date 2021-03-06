package mycontroller;

import java.util.HashMap;

import controller.CarController;
import mycontroller.strategies.AIStrategy;
import mycontroller.strategies.IMovementStrategy;
import mycontroller.util.Move;
import mycontroller.util.Move.Acceleration;
import mycontroller.util.Move.RelativeDirection;
import mycontroller.util.Path;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import mycontroller.AStarSearch;


/***
 * This class serves as the central controlling unit for the car AI. The main
 * purpose of this class is configuring the movement of the car.
 * */

public class MyAIController extends CarController {
	private IMovementStrategy strategy; //the AI traversal strategy
		
	public MyAIController(Car car) {
		super(car);
		HashMap<Coordinate, MapTile> worldMap = getMap();
		new CarSensor(worldMap, car); 
		new AStarSearch();
		strategy = new AIStrategy(worldMap);
	}

	/**
	 * Update() is responsible for reading in the current view of the car to the CarSensor,
	 * updating the state of the AI strategy and updating the car's movement
	 * */
	@Override
	public void update() {
		HashMap<Coordinate, MapTile> currentView = getView();
		CarSensor.updateMap(currentView);
		strategy.updateState(currentView);
		Move nextMove = strategy.move();	
		moveCar(nextMove);
	}
	
	/**
	 * This is responsible for controlling the movement of the car based of the AI strategy
	 * @param move - The next move suggested by the AI strategy
	 * */
	private void moveCar(Move move) {
		Acceleration idealAcceleration = move.getAcceleration();
		RelativeDirection relativeDirection = move.getRelativeDirection();
		int currentVelocity = CarSensor.getVelocity();
		
		//if movement is invalid, recalculate current strategy
		if(Path.invalidMove(move)) {
			strategy.reset();
			applyBrake();
			return;
		};
		
		turn(move, relativeDirection); 
		
		if(handleBlocked(move, currentVelocity)) return; 
		
		if(idealAcceleration.equals(Move.Acceleration.ACCELERATE)) {
			accelerate(move, currentVelocity, relativeDirection);
			
		} else if (idealAcceleration.equals(Move.Acceleration.BRAKE)){
			applyBrake();

		} else if (idealAcceleration.equals(Move.Acceleration.DECELERATE)){
			decelerate(move, currentVelocity, relativeDirection);
			
		} else {
			neutral(move, currentVelocity, relativeDirection);
		}
	}
	
	/**
	 * This is responsible for handling turns
	 * @param move - The next move suggested by the AI strategy
	 * @param relativeDirection 
	 * */
	private void turn(Move move, RelativeDirection relativeDirection) {
		if(relativeDirection.equals(Move.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (relativeDirection.equals(Move.RelativeDirection.RIGHT)) {
			turnRight();
		} 
	}
	
	/**
	 * This is responsible for the case where the current path of the car is blocked
	 * @param move - The next move suggested by the AI strategy
	 * */
	private boolean handleBlocked(Move move, int currentVelocity) {
		//if car is blocked in front
		if(CarSensor.isBlocked(move.getCurrent(), move.getDirection())) {
			//reverse
			if(currentVelocity >= 0) {
				applyReverseAcceleration();
			} 
			return true;
		//if car is blocked behind
		} else if (CarSensor.isBlocked(move.getCurrent(), WorldSpatial.reverseDirection(move.getDirection()))) {
			//go forward
			if(currentVelocity <= 0) {
				applyForwardAcceleration();
			}
			return true;
		} 
		return false;
	}
	
	/**
	 * This is responsible for the acceleration of the car
	 * @param move - The next move suggested by the AI strategy
	 * @param currentVelocity 
	 * @param relativeDirection 
	 * */
	private void accelerate(Move move, int currentVelocity, RelativeDirection relativeDirection) {
		if(relativeDirection.equals(Move.RelativeDirection.FORWARD)) {
			//increase velocity if greater than the minimum stable speed 
			if(currentVelocity < Move.MIN_FORWARD_SPEED) {
				applyForwardAcceleration();
			}
			
		} else if (relativeDirection.equals(Move.RelativeDirection.REVERSE)) {
			//reduce velocity if greater than the minimum stable speed
			if(currentVelocity > Move.MIN_REVERSE_SPEED) {
				applyReverseAcceleration();
			}
		} else {
			//if zero velocity and car needs to turn left/right
			if(currentVelocity == 0) {
				applyForwardAcceleration();
			}
		}
	}
	
	/**
	 * This is responsible for maintaining the movement of the car at a constant speed 
	 * @param move - The next move suggested by the AI strategy
	 * @param currentVelocity, RelativeDirection relativeDirection 
	 * */
	private void neutral(Move move, int currentVelocity, RelativeDirection relativeDirection) {
		
		if(relativeDirection.equals(Move.RelativeDirection.FORWARD)) {
			//tend towards minimum positive velocity
			if(currentVelocity > Move.MIN_FORWARD_SPEED) {
				applyReverseAcceleration();
			} else if (currentVelocity < Move.MIN_FORWARD_SPEED){
				applyForwardAcceleration();
			}
			
		} else if (relativeDirection.equals(Move.RelativeDirection.REVERSE)) {
			//tend towards minumim negative velocity
			if(currentVelocity < Move.MIN_REVERSE_SPEED) {
				applyForwardAcceleration();
			} else if (currentVelocity > Move.MIN_REVERSE_SPEED){
				applyReverseAcceleration();
			}
		} else {
			//if zero velocity and car needs to turn left/right
			if(currentVelocity == 0) {
				applyForwardAcceleration();
			}
		}
	}

	/**
	 * This is responsible for decelerating the movement of the car 
	 * @param move - The next move suggested by the AI strategy
	 * @param relativeDirection 
	 * @param currentVelocity 
	 * */
	private void decelerate(Move move, int currentVelocity, RelativeDirection relativeDirection) {
		if(relativeDirection.equals(Move.RelativeDirection.FORWARD)) {
			//reduce speed if greater than the minimum stable speed
			if(currentVelocity > Move.MIN_FORWARD_SPEED) {
				applyReverseAcceleration();
			}
			//increase speed if less than the minimum stable speed
		} else if (relativeDirection.equals(Move.RelativeDirection.REVERSE)) {
			if(currentVelocity < Move.MIN_REVERSE_SPEED) {
				applyForwardAcceleration();
			}
		} else {
			//if zero velocity and car needs to turn left/right
			if(currentVelocity == 0) {
				applyForwardAcceleration();
			}
		}
	}
}


