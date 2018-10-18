package mycontroller;

import java.util.HashMap;

import controller.CarController;
import mycontroller.strategies.AIStrategy;
import mycontroller.strategies.BasicStrategy;
import mycontroller.strategies.IMovementStrategy;
import mycontroller.util.Move;
import mycontroller.util.Path;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import mycontroller.AStarSearch;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	
	private Car car;
		
	public MyAIController(Car car) {
		super(car);
		this.car = car;
		HashMap<Coordinate, MapTile> worldMap = getMap();
		new WorldSensor(worldMap, car);
		new AStarSearch();
		strategy = new AIStrategy(worldMap);
	}

	@Override
	public void update() {
		HashMap<Coordinate, MapTile> currentView = getView();
		WorldSensor.updateMap(currentView);
		strategy.updateState(currentView);
		Move nextMove = strategy.move(WorldSensor.getWorldMap());	


		moveCar(nextMove);
	}

	private boolean isStopped() {
		return car.getVelocity() == 0;
	}
	
	private void moveCar(Move move) {
		System.out.println("Current: " + move.getCurrent());
		System.out.println("Target: " + move.getTarget());
		System.out.println("Direction: " + move.getDirection());
		System.out.println("Relative: " + move.getRelativeDirection());
		System.out.println("Acceleration: " + move.getAcceleration());
		System.out.println("Velocity: " + WorldSensor.getVelocity());
		System.out.println();
		
		if(Path.invalidMove(move)) {
			strategy.reset(WorldSensor.getWorldMap());
			return;
		};
			
		if(move.getRelativeDirection().equals(Move.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.RIGHT)) {
			turnRight();
		} 
		
		if(WorldSensor.isBlocked(move.getCurrent(), move.getDirection())) {
			if(WorldSensor.getVelocity() >= 0) {
				applyReverseAcceleration();
			} 
			return;
		} else if (WorldSensor.isBlocked(move.getCurrent(), WorldSpatial.reverseDirection(move.getDirection()))) {
			if(WorldSensor.getVelocity() <= 0) {
				applyForwardAcceleration();
			}
			return;
		} 
		
		if(move.getAcceleration().equals(Move.Acceleration.ACCELERATE)) {
			accelerate(move);
		} else if (move.getAcceleration().equals(Move.Acceleration.BRAKE)){
			applyBrake();
			System.out.println("BREAKING");
		} else if (move.getAcceleration().equals(Move.Acceleration.DECELERATE)){
			decelerate(move);
		} else {
			neutral(move);
		}
	}
	
	private void accelerate(Move move) {
		if(move.getRelativeDirection().equals(Move.RelativeDirection.FORWARD)) {
			if(WorldSensor.getVelocity() <= 0) {
				applyForwardAcceleration();
			}
			
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.REVERSE)) {
			if(WorldSensor.getVelocity() >= 0) {
				applyReverseAcceleration();
			}
		} else {
			if(WorldSensor.getVelocity() == 0) {
				applyForwardAcceleration();
			}
		}
	}
	
	private void neutral(Move move) {
		if(move.getRelativeDirection().equals(Move.RelativeDirection.FORWARD)) {
			if(WorldSensor.getVelocity() > Move.MIN_FORWARD_SPEED) {
				applyReverseAcceleration();
			} else if (WorldSensor.getVelocity() < Move.MIN_FORWARD_SPEED){
				applyForwardAcceleration();
			}
			
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.REVERSE)) {
			if(WorldSensor.getVelocity() < Move.MIN_REVERSE_SPEED) {
				applyForwardAcceleration();
			} else if (WorldSensor.getVelocity() < Move.MIN_REVERSE_SPEED){
				applyReverseAcceleration();
			}
		} else {
			if(WorldSensor.getVelocity() == 0) {
				applyForwardAcceleration();
			}
		}
	}
	
	private void decelerate(Move move) {
		if(move.getRelativeDirection().equals(Move.RelativeDirection.FORWARD)) {
			if(WorldSensor.getVelocity() > Move.MIN_FORWARD_SPEED) {
				applyReverseAcceleration();
			}
			
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.REVERSE)) {
			if(WorldSensor.getVelocity() < Move.MIN_REVERSE_SPEED) {
				applyForwardAcceleration();
			}
		} else {
			if(WorldSensor.getVelocity() == 0) {
				applyForwardAcceleration();
			}
		}
	}
}


