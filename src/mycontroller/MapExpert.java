package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import tiles.LavaTrap;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class MapExpert {
	private HashMap<Coordinate, MapTile.Type> worldMap = new HashMap<>();
	private HashMap<Coordinate, Boolean> explored = new HashMap<>();
	
	public MapExpert(HashMap<Coordinate, MapTile> worldTiles) {
		super();		
		HashMap<Coordinate, MapTile.Type> worldMap = new HashMap<>();
		for (Entry<Coordinate, MapTile>  entry : worldTiles.entrySet()) {
			worldMap.put(entry.getKey(),entry.getValue().getType());
		}
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
		for(Coordinate key : updates.keySet()) {
			worldMap.put(key, updates.get(key).getType());
			explored.put(key, true);
		}
		updateKey(updates);
	}
	
	public void updateKey(HashMap<Coordinate, MapTile> updates) {
		for (Entry<Coordinate, MapTile>  entry : updates.entrySet()) {
			MapTile tile = entry.getValue();
			if(tile instanceof LavaTrap) {
				if(((LavaTrap) tile).getKey()!=0) {
					System.out.println("found key in"+entry.getKey());
					KeyStrategy.addKey(entry.getKey());
				}
			}
		}
	}

	
	public HashMap<Coordinate, MapTile.Type> getWorldMap() {
		return worldMap;
	}
	
	public boolean hasSeenTile(Coordinate coor) {
		return explored.get(coor);
	}
	
	public void getKey(HashMap<Coordinate, MapTile> currentView){
		for (Entry<Coordinate, MapTile>  entry : currentView.entrySet()) {
			
		}		
	}
	
}
