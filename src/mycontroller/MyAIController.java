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
	
	HashMap<Coordinate, MapTile> worldView;
	
	public MyAIController(Car car) {
		super(car);
		this.car = car;
		strategy = new AIStrategy();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = getView();
		worldView.putAll(currentView);

		Coordinate currentPos =  new Coordinate(getPosition());
;
		Coordinate nextPos = strategy.move(currentPos, worldView);
		
		if(initializeFlag) {
			generateMapExpert();
			initializeFlag = !initializeFlag;
		}
		HashMap<Coordinate, MapTile> currentView = getView();
		mapExpert.updateMap(currentView);
		mapExpert.markTiles(currentView);
	}
	
	public void generateMapExpert() {
		HashMap<Coordinate,MapTile> currentMap = getMap();
		HashMap<Coordinate, MapTile.Type> worldMap = new HashMap<>();
		for (Entry<Coordinate, MapTile>  entry : currentMap.entrySet()) {
			worldMap.put(entry.getKey(),entry.getValue().getType());
		}
		mapExpert = new MapExpert(worldMap);
		System.out.println(mapExpert.getNeighbours(new Coordinate(2,17)));
		System.out.println(worldMap);
		System.out.println(World.MAP_WIDTH);
		System.out.println(World.MAP_HEIGHT);
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
				changeDirection(nextDirection);
			}
		}
		
		changeOrientation(getOrientation(), nextDirection);
		changeDirection(nextDirection);
	}
	
	private void changeOrientation(Direction current, Direction next) {
		while(!current.equals(next) && !next.equals(WorldSpatial.reverseDirection(current))) {
			if (next.equals(leftDirection(current))) {
				turnLeft();
			} else if (next.equals(rightDirection(current))) {
				turnRight();
			}
			current = getOrientation();
		}
	}
	
	public Direction getGlobalOrientation() {
		return getOrientation();
	}
	
	public MyAIController getInstance() {
		return this;
	}
	
	private void changeDirection(Direction nextDirection) {
		if(nextDirection.equals(getOrientation())) {
			goForward();
		} else {
			goBackward();
		}
	}
	
	private void goForward() {
		Coordinate currentPos =  new Coordinate(getPosition());
		MapTile tile = worldView.get(new Coordinate(currentPos.x, currentPos.y));
		
		while(car.getVelocity() < maxForwardVelocity) {
			applyForwardAcceleration();
			
			//go faster over traps
			if (tile.isType(MapTile.Type.TRAP)) {
				applyForwardAcceleration();
				
				//update knowledge about max forward velocity (why is it private?????)
				if(car.getVelocity() > maxForwardVelocity) {
					maxForwardVelocity = car.getVelocity();
				}
			}
		}
	}
	
	private void goBackward() {
		Coordinate currentPos =  new Coordinate(getPosition());
		MapTile tile = worldView.get(new Coordinate(currentPos.x, currentPos.y));

		while(car.getVelocity() > maxReverseVelocity) {
			applyReverseAcceleration();
			
			//go faster over trap tiles 
			if (tile.isType(MapTile.Type.TRAP)) {
				applyReverseAcceleration();
				
				//update knowledge about max reverse velocity
				if(car.getVelocity() < maxReverseVelocity) {
					maxReverseVelocity = car.getVelocity();
				}
			}
		}
	}
	
	public Direction leftDirection(Direction curr) {
		switch (curr) {
			case NORTH:
				return Direction.WEST;
			case SOUTH:
				return Direction.EAST;
			case EAST:
				return Direction.NORTH;
			case WEST:
				return Direction.SOUTH;
		}
		return Direction.NORTH; // should never happen
	}
	
	public Direction rightDirection(Direction curr) {
		switch (curr) {
			case NORTH:
				return Direction.EAST;
			case SOUTH:
				return Direction.WEST;
			case EAST:
				return Direction.SOUTH;
			case WEST:
				return Direction.NORTH;
		}
		return Direction.NORTH; // should never happen
	}
	
	private boolean isStopped() {
		return car.getVelocity() == 0;
	}
	
	private final Coordinate NORTH = new Coordinate(0, 1);
	private final Coordinate SOUTH = new Coordinate(1, 0);
	private final Coordinate EAST = new Coordinate(0, -1);
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
	private final Coordinate NORTH = new Coordinate(0, 1);
	private final Coordinate SOUTH = new Coordinate(1, 0);
	private final Coordinate EAST = new Coordinate(0, -1);
	private final Coordinate WEST = new Coordinate(-1, 0);
	
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


