package mycontroller.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

import mycontroller.Path;
import mycontroller.WorldSensor;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy extends BasicStrategy {
	static final int NODES_OUTSIDE_VIEW = 40; //number of nodes bordering view of Car
	Stack<Coordinate> paths;
	public static final Coordinate invalid = new Coordinate(-1, -1);
	ExploreStrategy(HashMap<Coordinate, MapTile> map){
		populateUnexploredTiles(map);
		paths = new Stack<>();
	}
	
	@Override
	public Coordinate move(HashMap<Coordinate, MapTile> worldView) {		
		if(path.endPath()) {
			if (goal.size() > 40) {
				path = new Path(worldView, WorldSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
				paths = path.getPath(worldView, WorldSensor.getCurrentPosition(), new ArrayList<>(goal.subList(0, NODES_OUTSIDE_VIEW)));
			}
			else {
				path = new Path(worldView, WorldSensor.getCurrentPosition(), goal);
				paths = path.getPath(worldView, WorldSensor.getCurrentPosition(), goal);
			}
			
		}
		
		

		if(paths.empty()) {
			
			return invalid;
		}
		else {
			for (Coordinate c : path.nullPath) {
				goal.remove(c);
			}
			//System.out.println(x);
			System.out.println(goal);
			return paths.pop();
		}
//		System.out.println("null" + path.nullPath);
//		System.out.println("before" + goal);
		
		//System.out.println("after" + goal);

		//return path.getNextMove();
	}
	
	public void updateState(HashMap<Coordinate, MapTile> state) {
		for(Coordinate coordinate : state.keySet()) {
			if(goal.contains(coordinate)) {
				goal.remove(coordinate);
			}
		}
		
        Collections.sort(goal, new CoordinateComparator(WorldSensor.getCurrentPosition()));
	}
	
	
	class CoordinateComparator implements Comparator<Coordinate> {
		Coordinate currentPosition;
		
		CoordinateComparator(Coordinate position){
			this.currentPosition = position;
		}
		
		@Override
	    public int compare(Coordinate c1, Coordinate c2)
	    {	
			//compare points based on euclidean distance from current position
			return Double.compare(dist(currentPosition, c1), dist(currentPosition, c2));
	    }
		
		private double dist(Coordinate c1, Coordinate c2) {
			return Math.sqrt(Math.pow((c1.x - c2.x), 2) + Math.pow((c1.y - c2.y), 2));
		}
	};
	
	
	//adds unexplored tiles to goal array
	private void populateUnexploredTiles(HashMap<Coordinate, MapTile> map) {
		for(Coordinate coordinate : map.keySet()) {
			MapTile tile = map.get(coordinate);
			
			if(tile.getType().equals(MapTile.Type.ROAD) || tile.getType().equals(MapTile.Type.TRAP)) {
				//TODO() if tile has a path to it (not blocked by mud/walls)
				goal.add(coordinate);
				
			}
		}
        Collections.sort(goal, new CoordinateComparator(WorldSensor.getCurrentPosition()));
	}
}
