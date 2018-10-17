package mycontroller;

import java.util.HashMap;

import controller.CarController;
import mycontroller.strategies.AIStrategy;
import mycontroller.strategies.IMovementStrategy;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.Direction;
import mycontroller.AStarSearch;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	private WorldSensor sensor;
	enum RelativeDirection {LEFT, RIGHT, FORWARD, BACKWARD}
	
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
		Coordinate currentPos =  new Coordinate(getPosition());

		HashMap<Coordinate, MapTile> currentView = getView();
		sensor.updateMap(currentView);
		strategy.updateState(currentView);
		
		Coordinate nextPos = strategy.move(sensor.getWorldMap());
		
		coordinateToMovement(currentPos, nextPos);
	}

	private void coordinateToMovement(Coordinate current, Coordinate next) {
		if(Path.invalidMove(next)) {
			return;
		}
//		System.out.println(sensor.getWorldMap().get(next).getType());
//		System.out.println(current);
//		System.out.println(next);
		
		Direction nextDirection = absoluteToRelativePosition(current, next);

		if(current.equals(next)) {
			
			while(!isStopped()) {
				applyBrake();
			}
			return;
		} else {
			if(isStopped()) {
				//apply acceleration so car can turn
				applyForwardAcceleration();
				//applyBrake();
				//changeAcceleration(nextDirection);
				
			}
		}
		
		changeOrientation(getOrientation(), nextDirection);
		applyForwardAcceleration();
		//applyBrake();
		//applyReverseAcceleration();
		//changeAcceleration(nextDirection);
	}
	
	private void changeOrientation(Direction current, Direction next) {
		
		if (!current.equals(next) && !next.equals(WorldSpatial.reverseDirection(current))) {
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
		System.out.println(getOrientation());
		System.out.println(nextDirection);
		
		if(nextDirection.equals(getOrientation())) {
			applyForwardAcceleration();
		} else {
			applyReverseAcceleration();
		}
	}
	
	private boolean isStopped() {
		return car.getVelocity() == 0;
	}
	
	public Direction absoluteToRelativePosition(Coordinate current, Coordinate next) {
		
		Coordinate pos = new Coordinate(next.x - current.x, next.y - current.y);
		
//		if(pos.equals(NORTH)) {
//			return Direction.NORTH;
//			
//		} else if (pos.equals(SOUTH)) {
//			return Direction.SOUTH;
//			
//		} else if (pos.equals(EAST)) {
//			return Direction.EAST;
//			
//		}else if (pos.equals(WEST)) {
//			return Direction.WEST;
//			
//		} else {
//			
//			return null;
//		}
		
		if (pos.x > 0) {
			return Direction.EAST;
		}
		else if (pos.y < 0) {
			return Direction.SOUTH;
			
		} else if (pos.x < 0) {
			return Direction.WEST;
			
		}else if (pos.y > 0) {
			return Direction.NORTH;
			
		} else {
			
			return null;
		}
	}
	
	private final Coordinate NORTH = new Coordinate(0, 1);
	private final Coordinate EAST = new Coordinate(1, 0);
	private final Coordinate SOUTH = new Coordinate(0, -1);
	private final Coordinate WEST = new Coordinate(-1, 0);
}


