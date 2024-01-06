package agh.ics.oop.model;

public class Plant {
    private int energy;
    private Boolean poisonous;
    private Vector2d position;

    public Plant(Vector2d position, boolean poisonous){
        this.energy = 10;
        this.position = position;
        this.poisonous = poisonous;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    public boolean isPoisonous(){
        return poisonous;
    }
    public void setPoisonStatus(boolean value){
        this.poisonous = value;
        if(value == true){
            this.energy = (-1)*energy;
        }
    }
    public int getEnergy(){
        return this.energy;
    }
}
