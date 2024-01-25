package agh.ics.oop.model;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Jungle {
    private int width;
    private int height;
    private Coordinates coordinates;
    private int area;
    private DarwinWorld owner; //world that owns this jungle
    private PoisonousArea poisonousArea;

    public Jungle(int width, int height, Coordinates coordinates, DarwinWorld owner, PoisonousArea poisonousArea){
        this.width = width;
        this.height = height;
        this.area = width*height;
        this.coordinates = coordinates;
        this.owner = owner;
        this.poisonousArea = poisonousArea;
    }
    public void randomPlacePlant(){
        int randX = (int)(Math.random()*(this.width));
        int randY = (int)(Math.random()*(this.height)) + this.coordinates.getBottom();
        //Random rand = new Random();
        //int randX = rand.nextInt(this.width);
        //int randY = rand.nextInt(this.height) + this.coordinates.getBottom();
        Vector2d position = new Vector2d(randX, randY);
        if(owner.isOccupiedByPlant(position) == false){
            Plant plant = new Plant(position, false, owner.getPlantEnergy());
            if(poisonousArea != null){
                if(poisonousArea.doesFit(position)){
                    plant.setPoisonStatus(true);
                }
            }
            owner.placePlant(position, plant);
        }
    }

    public Coordinates getCoordinates(){
        return this.coordinates;
    }
    public boolean fitsToMap(Vector2d position){
        if(position.getX() >= this.coordinates.getLeftSide() &&
        position.getX() <= this.coordinates.getRightSide() &&
        position.getY() >= this.coordinates.getBottom() &&
        position.getY() <= this.coordinates.getCeiling()){
            return true;
        }
        return false;
    }
    public int getHeight(){
        return this.height;
    }

}
