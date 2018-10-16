package mycontroller;

import java.util.Arrays;
import java.util.HashMap;

import java.util.Map.Entry;
import controller.CarController;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial.Direction;
import world.World;

public class MyAIController extends CarController {
	IMovementStrategy strategy;
	private boolean initializeFlag = true;
	private MapExpert mapExpert;

	public MyAIController(Car car) {
		super(car);
		strategy = new AIStrategy();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = getView();
		worldView.putAll(currentView);

		Coordinate next = strategy.move(worldView);
		
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
	
	private void coordinateToMovement(Coordinate next) {
		Coordinate current = toCoordinate(getPosition());

		Direction nextDir = positionDelta(new Coordinate(next.x - current.x, next.y - current.y));
		
		Direction currDir = getOrientation();
		
		if(nextDir!=null && currDir!=null) {
			while(!currDir.equals(nextDir)) {
				turnRight();
				currDir = getOrientation();
			}
		}
		
		
	}
	
	public Direction positionDelta(Coordinate pos) {
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
	
	private Coordinate toCoordinate(String position) {
		int[] pos = Arrays.stream(position
				.replace("(", "")
				.replace(")", "")
				.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
		return new Coordinate(pos[0], pos[1]);
		
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


