package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

public class DarwinWorld {
    private Map<Vector2d, Animal> animals = new HashMap<>();
    private Map<Coordinates, Jungle> jungles = new HashMap<>();
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private ArrayList<Vector2d> jungleSquares;
    protected int width;
    protected int height;
    protected int ID;
    protected int day;
    private int plantAmount;
    private int dailyPlants;



    public DarwinWorld(int width, int height, int ID, int plantAmount, int dailyPlants){
        this.width = width;
        this.height = height;
        this.ID = ID;
        this.plantAmount = plantAmount;
        this.jungleSquares = new ArrayList<>();
        setJungle();
       // setSteppesPlants();
        removePlants();
        addPlants();
    }
    public void placePlant(Vector2d position, Plant plant){
        if(isOccupiedByAnimal(position) == false && isOccupiedByPlant(position) == false){
            plants.put(position, plant);
            if(jungleSquares.contains(position)){
                for(Map.Entry<Coordinates, Jungle> entry : jungles.entrySet()){
                    Jungle value = entry.getValue();
                    if(position.getX() >= value.getCoordinates().getLeftSide() &&
                    position.getX() <= value.getCoordinates().getRightSide() &&
                    position.getY() >= value.getCoordinates().getBottom() &&
                    position.getY() <= value.getCoordinates().getCeiling()){
                        value.placePlant(position, plant);
                    }
                }
            }
        }
    }
    public void placeAnimal(Vector2d position, Animal animal){
        if(isOccupiedByPlant(position) == false && isOccupiedByAnimal(position) == false){
            animals.put(position, animal);
        } if(isOccupiedByPlant(position) == true && isOccupiedByAnimal(position) == false) {
            plants.remove(position);
            animals.put(position, animal);
        }
    }
    public boolean isOccupiedByPlant(Vector2d position){
        if(plants.get(position) != null){
            return true;
        }
        return false;
    }
    public boolean isOccupiedByAnimal(Vector2d position){
        if(animals.get(position) != null){
            return true;
        }
        return false;
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
               currArea = addJungleSquares(Math.min(randX1, randX2), Math.max(randX1, randX2),
               Math.min(randY1, randY2), Math.max(randY1, randY2), currArea);
               jungles.put(coordinates, jungle);
           }
        }
    }

    public boolean areaCheck(int X1, int X2, int Y1, int Y2, int jungleArea, int currArea){
        int counter = 0;
        for(int i = X1; i < X2; i++){
            for(int z = Y1; z < Y2; z++){
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
        for(int i = X1; i < X2; i++){
            for(int z = Y1; z < Y2; z++){
                Vector2d square = new Vector2d(i, z);
                if(jungleSquares.contains(square) == false){
                    currArea++;
                    jungleSquares.add(square);
                }
            }
        }
        return currArea;
    }

    public void setSteppesPlants(){
        Random rand = new Random();
        double randPercentage = 0.05 + (0.1) * rand.nextDouble();
        int steppesPlantsAmount = (int)(0.7*width*height*randPercentage);
        int counter = 0;
        while(counter < steppesPlantsAmount){
            int randX = rand.nextInt(width);
            int randY = rand.nextInt(height);
            Vector2d position = new Vector2d(randX, randY);
            if(plants.get(position) == null && !jungleSquares.contains(position)){
                plants.put(position, new Plant(position));
                counter++;
            }
        }
    }

    public void removePlants(){
        Random rand = new Random();
        while(plants.size() > plantAmount){
            int randX = rand.nextInt(width);
            int randY = rand.nextInt(height);
            Vector2d position = new Vector2d(randX, randY);
            if(plants.get(position) != null){
                plants.remove(position);
                for(Map.Entry<Coordinates, Jungle> entry : jungles.entrySet()){
                    Jungle value = entry.getValue();
                    value.removePlant(position);
                }
            }
        }
    }

    public void addPlants(){
        Random rand = new Random();
        while(plants.size()< plantAmount){
            int randX = rand.nextInt(width);
            int randY = rand.nextInt(height);
            Vector2d position = new Vector2d(randX, randY);
            if(plants.get(position) == null && !jungleSquares.contains(position)){
                plants.put(position, new Plant(position));
            }
            if(plants.get(position) == null && jungleSquares.contains(position)){
                Plant plant = new Plant(position);
                plants.put(position, plant);
                for(Map.Entry<Coordinates, Jungle> entry : jungles.entrySet()){
                    Jungle value = entry.getValue();
                    if(position.getX() <= value.getCoordinates().getRightSide() &&
                    position.getX() >= value.getCoordinates().getLeftSide() &&
                    position.getY() <= value.getCoordinates().getCeiling() &&
                    position.getY() >= value.getCoordinates().getBottom()){
                        value.placePlant(position, plant);
                    }
                }

            }
        }
    }

    public void updateDay(){
        this.day = day + 1;
        dailyPlantUpdate();
    }
    public void dailyPlantUpdate(){


    }

    public Map<Vector2d, Plant> getPlants(){
        return plants;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector2d position = new Vector2d(x, y);
                if (jungleSquares.contains(position)) {
                    result.append("*");  // Jungle
                } else {
                    result.append(plants.containsKey(position) ? "*" : "|");  // Plant or edge
                }
            }
            result.append("\n");
        }

        return result.toString();
    }


}
