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
import world.WorldSpatial.RelativeDirection;
import world.World;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	private boolean initializeFlag = true;
	private MapExpert mapExpert;

	enum RelativeDirection {LEFT, RIGHT, FORWARD, BACKWARD}
	
	private Car car;
	private int maxForwardVelocity = 1;
	private int maxReverseVelocity = -1;
		
	public MyAIController(Car car) {
		super(car);
		this.car = car;
		strategy = new AIStrategy();
		HashMap<Coordinate, MapTile> worldMap = getMap();
		mapExpert = new MapExpert(worldMap);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = getView();
		mapExpert.updateMap(currentView);
		
		Coordinate currentPos =  new Coordinate(getPosition());
		
		Coordinate nextPos = strategy.move(getOrientation(), currentPos, mapExpert.getWorldMap());
		
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
	
	
	
	public void getViewSpecifics(HashMap<Coordinate, MapTile> currentView){
		for (Entry<Coordinate, MapTile>  entry : currentView.entrySet()) {
			MapTile thing = entry.getValue();
			if(thing instanceof LavaTrap) {
				if(((LavaTrap) thing).getKey()!=0) {
					System.out.println("found key in"+entry.getKey());
					mapExpert.addKey(entry.getKey());
				}
			}
		}		
	}
		
	
}


