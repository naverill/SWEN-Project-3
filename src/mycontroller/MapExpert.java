package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class MapExpert {
	private HashMap<Coordinate, MapTile.Type> worldMap;
	private HashMap<Coordinate, MapTile.Type> markedTiles;
	private ArrayList<Coordinate> keys;
	
	public MapExpert(HashMap<Coordinate, Type> worldMap) {
		super();
		this.worldMap = worldMap;
		this.keys = new ArrayList<>();
	}
	
	public void addKey(Coordinate key) {
		if(!keys.contains(key)) {
			keys.add(key);
			System.out.println(keys);
		}
	}
	
	public void removeKey(Coordinate key) {
		keys.remove(key);
	}
	
	public  HashMap<Coordinate, MapTile.Type> getNeighbours(Coordinate key){
		HashMap<Coordinate, MapTile.Type> neighbours = new HashMap<>();
		int xValue = key.x;
		int yValue = key.y;
				
		Coordinate northNeighbourCoor = new Coordinate(xValue, yValue+1);
		Coordinate eastNeighbourCoor = new Coordinate(xValue+1, yValue);
		Coordinate westNeighbourCoor = new Coordinate(xValue-1, yValue);
		Coordinate southNeighbourCoor = new Coordinate(xValue, yValue-1);
		
		MapTile.Type northNeighbourType = worldMap.get(northNeighbourCoor);
		MapTile.Type eastNeighbourType = worldMap.get(eastNeighbourCoor);
		MapTile.Type westNeighbourType = worldMap.get(westNeighbourCoor);
		MapTile.Type southNeighbourType = worldMap.get(southNeighbourCoor);

		neighbours.put(northNeighbourCoor, northNeighbourType);
		neighbours.put(eastNeighbourCoor, eastNeighbourType);
		neighbours.put(westNeighbourCoor, westNeighbourType);
		neighbours.put(southNeighbourCoor, southNeighbourType);
		
		return neighbours;
	}
	
	public void updateMap(HashMap<Coordinate, MapTile> updates) {
		HashMap<Coordinate, MapTile.Type> updatedPoints = new HashMap<>();
		for (Entry<Coordinate, MapTile>  entry : updates.entrySet()) {
			updatedPoints.put(entry.getKey(),entry.getValue().getType());
		}
		this.worldMap.putAll(updatedPoints); 
	}
	
	public void markTiles(HashMap<Coordinate, MapTile> tiles) {
		HashMap<Coordinate, MapTile.Type> tilesToMark= new HashMap<>();
		for (Entry<Coordinate, MapTile>  entry : tiles.entrySet()) {
			tilesToMark.put(entry.getKey(),entry.getValue().getType());
		}
		this.worldMap.putAll(tilesToMark); 
	}

	public HashMap<Coordinate, MapTile.Type> getWorldMap() {
		return worldMap;
	}
	
}
