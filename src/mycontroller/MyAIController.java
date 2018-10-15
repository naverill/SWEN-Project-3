package mycontroller;

import java.util.HashMap;
import java.util.Map.Entry;

import controller.CarController;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;

public class MyAIController extends CarController{
	IMovementStrategy strategy;
	private boolean initializeFlag = true;
	private MapExpert mapExpert;

	public MyAIController(Car car) {
		super(car);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		HashMap<Coordinate, MapTile> currentView = getView();
		
		if(initializeFlag) {
			generateMapExpert();
			initializeFlag = !initializeFlag;
		}
	}
	
	public void generateMapExpert() {
		HashMap<Coordinate,MapTile> currentMap = getMap();
		HashMap<Coordinate, MapTile.Type> worldMap = new HashMap<>();
		for (Entry<Coordinate, MapTile>  entry : currentMap.entrySet()) {
			worldMap.put(entry.getKey(),entry.getValue().getType());
		}
		for (Entry<Coordinate, MapTile>  entry : currentMap.entrySet()) {
			worldMap.put(entry.getKey(),entry.getValue().getType());
		}
		mapExpert = new MapExpert(worldMap);
		System.out.println(worldMap);
		System.out.println(World.MAP_WIDTH);
		System.out.println(World.MAP_HEIGHT);
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


