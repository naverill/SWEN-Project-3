package mycontroller;

import java.util.Arrays;
import java.util.HashMap;

import java.util.Map.Entry;
import controller.CarController;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	private WorldSensor sensor;

	enum RelativeDirection {LEFT, RIGHT, FORWARD, BACKWARD}
	
	private Car car;
		
	public MyAIController(Car car) {
		super(car);
		this.car = car;
		HashMap<Coordinate, MapTile> worldMap = getMap();
		sensor = new WorldSensor(worldMap);
		strategy = new AIStrategy(worldMap);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = getView();
		sensor.updateMap(currentView);
		strategy.updateState(currentView);
		
		Coordinate currentPos =  new Coordinate(getPosition());
		
		Coordinate nextPos = strategy.move(getOrientation(), currentPos, sensor.getWorldMap());
		
		coordinateToMovement(currentPos, nextPos);
	}
	
	private void coordinateToMovement(Coordinate current, Coordinate next) {
		Direction nextDirection = absoluteToRelativePosition(current, next);

		if(current.equals(next)) {
			while(!isStopped()) {
				applyBrake();
			}
			return;
		} else {
			if(isStopped()) {
				//apply acceleration so car can turn
				changeAcceleration(nextDirection);
			}
		}
		
		changeOrientation(getOrientation(), nextDirection);
		changeAcceleration(nextDirection);
	}
	
	private void changeOrientation(Direction current, Direction next) {
		while(!current.equals(next) && !next.equals(WorldSpatial.reverseDirection(current))) {
			//next direction is left of current
			if (next.equals(WorldSpatial.changeDirection(current, WorldSpatial.RelativeDirection.LEFT))) {
				turnLeft();
			//next direction is right of current
			} else if (next.equals(WorldSpatial.changeDirection(current, WorldSpatial.RelativeDirection.RIGHT))) {
				turnRight();
			}
			current = getOrientation();
		}
	}
	
	private void changeAcceleration(Direction nextDirection) {
		if(nextDirection.equals(getOrientation())) {
			applyForwardAcceleration();
		} else {
			applyReverseAcceleration();
		}
	}
	
	private boolean isStopped() {
		return car.getVelocity() == 0;
	}
	
	private final Coordinate NORTH = new Coordinate(0, 1);
	private final Coordinate EAST = new Coordinate(1, 0);
	private final Coordinate SOUTH = new Coordinate(0, -1);
	private final Coordinate WEST = new Coordinate(-1, 0);
	
	public Direction absoluteToRelativePosition(Coordinate current, Coordinate next) {
		Coordinate pos = new Coordinate(next.x - current.x, next.y - current.y);

		System.out.println("("+ next.x + "," + next.y + ")");
		System.out.println("("+ pos.x + "," + pos.y + ")");
		
		if(pos.equals(NORTH)) {
			return Direction.NORTH;
			
		} else if (pos.equals(SOUTH)) {
			return Direction.SOUTH;
			
		} else if (pos.equals(EAST)) {
			return Direction.EAST;
			
		}else if (pos.equals(WEST)) {
			return Direction.WEST;
			
		} else {
			return null;
		}
	}	
}


