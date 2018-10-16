package mycontroller;

	
import java.util.*;
import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;


public class AStar {
    //public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;
    
//    static class Cell{  
//        int heuristicCost = 0; //Heuristic cost
//    int finalCost = 0; //G+H
//    int i, j;
//    Cell parent; 
//    
//    Cell(int i, int j){
//        this.i = i;
//        this.j = j; 
//    }
    
    @Override
//    public String toString(){
//        return "["+this.i+", "+this.j+"]";
//    }
//}

//Blocked cells are just null Cell values in grid
//static Cell [][] grid = new Cell[5][5];
private HashMap<Coordinate, MapTile.Type> overall = new HashMap<Coordinate, MapTile.Type>();
private static PriorityQueue<Coordinate> open;
 
private static boolean closed[][];
//static int startI, startJ;
//static int endI, endJ;

private static Coordinate start;
private static Coordinate end;
        
public static void setBlocked(HashMap<Coordinate, MapTile.Type> overall, Coordinate blocked){
    if(overall.getValue(blocked))
}


static void checkAndUpdateCost(Coordinate current, Coordinate t, int cost){
    if(t == null || closed[t.i][t.j])return;
    int t_final_cost = t.heuristicCost+cost;
    
    boolean inOpen = open.contains(t);
    if(!inOpen || t_final_cost<t.finalCost){
        t.finalCost = t_final_cost;
        t.parent = current;
        if(!inOpen)open.add(t);
    }
}

public static void AStar(){ 
    
    //add the start location to open list.
    open.add(grid[startI][startJ]);
    
    Cell current;
    
    while(true){ 
        current = open.poll();
        if(current==null)break;
        closed[current.i][current.j]=true; 

        if(current.equals(grid[endI][endJ])){
            return; 
        } 

        Cell t;  
        if(current.i-1>=0){
            t = grid[current.i-1][current.j];
            checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

            if(current.j-1>=0){                      
                t = grid[current.i-1][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }

            if(current.j+1<grid[0].length){
                t = grid[current.i-1][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }
        } 

        if(current.j-1>=0){
            t = grid[current.i][current.j-1];
            checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
        }

        if(current.j+1<grid[0].length){
            t = grid[current.i][current.j+1];
            checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
        }

        if(current.i+1<grid.length){
            t = grid[current.i+1][current.j];
            checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

            if(current.j-1>=0){
                t = grid[current.i+1][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }
            
            if(current.j+1<grid[0].length){
               t = grid[current.i+1][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost); 
            }  
        }
    } 
}
}
