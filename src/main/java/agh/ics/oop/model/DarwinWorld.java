package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class DarwinWorld {
    private Map<Vector2d, Animal> animals = new HashMap<>();
    private Map<Coordinates, Jungle> jungles = new HashMap<>();
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private ArrayList<Vector2d> jungleSquares;
    protected int width;
    protected int height;
    protected int ID;
    protected int day;



    public void DarwinWorld(int width, int height, int ID){
        this.width = width;
        this.height = height;
        this.ID = ID;
        setJungle();
    }
    public void placePlant(Vector2d position, Plant plant){
        plants.put(position, plant);
    }

    public void setJungle(){
        int area = width*height;
        int jungleArea = (int)(0.3 * area); //In my version jungle stands for 30% of a map
        int currArea = 0;
        Random random = new Random();
        while(currArea < jungleArea){
           int randX1 = random.nextInt(width);
           int randX2 = random.nextInt(width);
           int randY1 = random.nextInt(height);
           int randY2 = random.nextInt(height);
           int possibleWidth = Math.abs(randX1 - randX2);
           int possibleHeight = Math.abs(randY1-randY2);
           boolean flag = areaCheck(Math.min(randX1, randX2), Math.max(randX1, randX2),
           Math.min(randY1, randY2), Math.max(randY1, randY2), jungleArea, currArea);
           if(flag){
               Coordinates coordinates = new Coordinates(Math.min(randX1, randX2), Math.max(randX1, randX2),
               Math.max(randY1, randY2), Math.min(randY1, randY2));
               Jungle jungle = new Jungle(possibleWidth, possibleHeight, coordinates, this);
               addJungleSquares(Math.min(randX1, randX2), Math.max(randX1, randX2),
               Math.min(randY1, randY2), Math.max(randY1, randY2), currArea);
               jungles.put(coordinates, jungle);
           }
        }
    }

    public boolean areaCheck(int X1, int X2, int Y1, int Y2, int jungleArea, int currArea){
        int counter = 0;
        for(int i = X1; i <= X2; i++){
            for(int z = Y1; z <= Y2; z++){
                Vector2d square = new Vector2d(i, z);
                if(jungleSquares.contains(square) == false){
                    counter ++;
                }
            }
        }
        if(counter + currArea <= jungleArea){
            return true;
        }
        return false;
    }

    public int addJungleSquares(int X1, int X2, int Y1, int Y2, int currArea){
        for(int i = X1; i <= X2; i++){
            for(int z = Y1; z <= Y2; z++){
                Vector2d square = new Vector2d(i, z);
                if(jungleSquares.contains(square) == false){
                    currArea++;
                    jungleSquares.add(square);
                }
            }
        }
        return currArea;
    }
    public void updateDay(){
        this.day = day + 1;
    }



}
