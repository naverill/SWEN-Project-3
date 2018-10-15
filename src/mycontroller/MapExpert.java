package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class MapExpert {
	private HashMap<Coordinate, MapTile.Type> worldMap;
	
	public MapExpert(HashMap<Coordinate, Type> worldMap) {
		super();
		this.worldMap = worldMap;
	}
	
	public void setWorldMap(HashMap<Coordinate, MapTile.Type> worldMap) {
		this.worldMap = worldMap;
	}

	public HashMap<Coordinate, MapTile.Type> getWorldMap() {
		return worldMap;
	}
}
