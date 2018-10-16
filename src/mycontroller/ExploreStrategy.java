package mycontroller;

import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class ExploreStrategy implements IMovementStrategy {
	
	private static HashMap<Coordinate,MapTile> viewed = new HashMap<Coordinate,MapTile>();
	private Car car;
	
	public ExploreStrategy(Car car) {
		this.car = car;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = car.getView();
		
		
	}
	
	public void storeSurround(HashMap<Coordinate, MapTile> currentView) {
		
		for (HashMap.Entry<Coordinate, MapTile> entry : currentView.entrySet()) {
			if(!viewed.containsKey(entry.getKey())) {
				viewed.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public void getFinish(HashMap<Coordinate, MapTile> currentView) {
		for (HashMap.Entry<Coordinate, MapTile> entry : currentView.entrySet()) {
			System.out.println(entry.getValue());
		}
	}

}
