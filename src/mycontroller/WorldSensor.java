package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class WorldSensor {
	static public HashMap<Coordinate, MapTile> map = new HashMap<>();
	static private Coordinate currentPosition;
	
	public WorldSensor(HashMap<Coordinate, MapTile> worldTiles, Coordinate position) {
		currentPosition = position;
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
	
	public void updateMap(HashMap<Coordinate, MapTile> view, Coordinate position) {
		currentPosition = position;
		map.putAll(view);
	}
	
	public HashMap<Coordinate, MapTile> getWorldMap() {
		return map;
	}
	
	static public Coordinate getCurrentPosition() {
		return new Coordinate(currentPosition.x, currentPosition.y);
	}

}
