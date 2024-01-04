package agh.ics.oop.model;

public class Plant {
    private int energy;
    private Vector2d position;

    public Plant(Vector2d position){
        this.energy = 10;
        this.position = position;
    }
    public Vector2d getPosition(){
        return this.position;
    }
}
