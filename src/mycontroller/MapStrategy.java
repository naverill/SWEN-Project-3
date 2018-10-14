package mycontroller;

import java.util.ArrayList;

public class MapStrategy implements IMovementStrategy {
	enum StrategyKey {
		kExploreStrat,
		kHealthStrat,
		kKeyStrat
	}
	
	public ArrayList<IMovementStrategy>  strategies = new ArrayList<>();

	public MapStrategy() {
		strategies.add((IMovementStrategy) new ExploreStrategy());
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

}
