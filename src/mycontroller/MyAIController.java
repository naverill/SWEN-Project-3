package mycontroller;

import java.util.HashMap;

import controller.CarController;
import mycontroller.strategies.AIStrategy;
import mycontroller.strategies.IMovementStrategy;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import mycontroller.AStarSearch;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	private WorldSensor sensor;
	
	private Car car;
		
	public MyAIController(Car car) {
		super(car);
		this.car = car;
		HashMap<Coordinate, MapTile> worldMap = getMap();
		sensor = new WorldSensor(worldMap, car);
		new AStarSearch(sensor);
		strategy = new AIStrategy(worldMap, sensor);
	}

	@Override
	public void update() {
		HashMap<Coordinate, MapTile> currentView = getView();
		sensor.updateMap(currentView);
		strategy.updateState(currentView);
		
		Move nextMove = strategy.move(sensor.getWorldMap());	
		
		moveCar(nextMove);
	}

	private boolean isStopped() {
		return car.getVelocity() == 0;
	}
	
	private void moveCar(Move move) {
//		System.out.println("Current: " + move.getCurrent());
//		System.out.println("Target: " + move.getTarget());
//		System.out.println("Direction: " + move.getDirection());
//		System.out.println("Relative: " + move.getRelativeDirection());
//		System.out.println("Acceleration: " + move.getAcceleration());
//		System.out.println("Velocity: " + WorldSensor.getVelocity());
//		System.out.println();
		
		if(Path.invalidMove(move)) {
			strategy.reset(sensor.getWorldMap());
			return;
		};
			
		if(move.getRelativeDirection().equals(Move.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.RIGHT)) {
			turnRight();
		} 
		
		if(move.getAcceleration().equals(Move.Acceleration.ACCELERATE)) {
			accelerate(move);

		} else if (move.getAcceleration().equals(Move.Acceleration.BRAKE)){
			if(!isStopped()) {
				applyBrake();
				strategy.applyBrake();
			} else {
				System.out.println("BRAKE");
			}
			
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
		}
	}
	
	private void neutral(Move move) {
		if(move.getRelativeDirection().equals(Move.RelativeDirection.FORWARD)) {
			if(WorldSensor.getVelocity() > Move.MIN_FORWARD_SPEED) {
				applyReverseAcceleration();
			} else {
				applyForwardAcceleration();
			}
			
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.REVERSE)) {
			if(WorldSensor.getVelocity() < Move.MIN_REVERSE_SPEED) {
				applyForwardAcceleration();
			} else {
				applyReverseAcceleration();
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
		}
	}
}


