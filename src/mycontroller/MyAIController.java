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
		strategy = new AIStrategy(worldMap);
		new AStarSearch(sensor);
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
		if(Path.invalidMove(move.getTarget())) return;
		
		if(move.getAcceleration().equals(Move.Acceleration.ACCELERATE)) {
			applyForwardAcceleration();
		} else if (move.getAcceleration().equals(Move.Acceleration.BRAKE)){
			applyBrake();
		} else {
			applyReverseAcceleration();
		}
			
		if(move.getRelativeDirection().equals(Move.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (move.getRelativeDirection().equals(Move.RelativeDirection.LEFT)) {
			turnRight();
		}
	}
}


