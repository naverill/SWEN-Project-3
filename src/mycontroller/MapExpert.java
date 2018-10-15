package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class MapExpert {
	private HashMap<Coordinate, MapTile.Type> worldMap;
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
	
	public void setWorldMap(HashMap<Coordinate, MapTile.Type> worldMap) {
		this.worldMap = worldMap;
	}

	public HashMap<Coordinate, MapTile.Type> getWorldMap() {
		return worldMap;
	}
}
