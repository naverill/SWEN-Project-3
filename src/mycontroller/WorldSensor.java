package mycontroller;

import java.util.HashMap;
import java.util.Set;

import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

public class WorldSensor {
	static public HashMap<Coordinate, MapTile> map = new HashMap<>();
	public static Car car;
	
	public WorldSensor(HashMap<Coordinate, MapTile> worldTiles, Car car) {
		this.car = car;
		map.putAll(worldTiles);
	}

	public  HashMap<Coordinate, MapTile> getNeighbours(Coordinate key){
		HashMap<Coordinate, MapTile> neighbours = new HashMap<>();
		int xValue = key.x;
		int yValue = key.y;
				
		Coordinate northNeighbourCoor = new Coordinate(xValue, yValue+1);
		Coordinate eastNeighbourCoor = new Coordinate(xValue+1, yValue);
		Coordinate westNeighbourCoor = new Coordinate(xValue-1, yValue);
		Coordinate southNeighbourCoor = new Coordinate(xValue, yValue-1);
		
		MapTile northNeighbourType = map.get(northNeighbourCoor);
		MapTile eastNeighbourType = map.get(eastNeighbourCoor);
		MapTile westNeighbourType = map.get(westNeighbourCoor);
		MapTile southNeighbourType = map.get(southNeighbourCoor);

		neighbours.put(northNeighbourCoor, northNeighbourType);
		neighbours.put(eastNeighbourCoor, eastNeighbourType);
		neighbours.put(westNeighbourCoor, westNeighbourType);
		neighbours.put(southNeighbourCoor, southNeighbourType);
		
		return neighbours;
	}
	
	public void updateMap(HashMap<Coordinate, MapTile> view) {
		map.putAll(view);
	}
	
	public HashMap<Coordinate, MapTile> getWorldMap() {
		return map;
	}
	
	public boolean hasAllKeys() {
		int numKeys = car.numKeys;
		Set<Integer> currKeys = car.getKeys();
		for (int i = 1; i <= numKeys; i++) if (!currKeys.contains(i)) return false;
		System.out.println("yeet");
		return true;
	}
	
	static public Coordinate getCurrentPosition() {
		return new Coordinate(car.getPosition());
	}

}
