package agh.ics.oop.model;

public class Coordinates {
    private int leftSide;
    private int rightSide;
    private int ceiling;
    private int bottom;

    public Coordinates(int leftSide, int rightSide, int ceiling, int bottom){
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.ceiling = ceiling;
        this.bottom = bottom;
    }
    public int getRightSide(){
        return this.rightSide;
    }
    public int getLeftSide(){
        return this.leftSide;
    }
    public int getCeiling(){
        return this.ceiling;
    }
    public int getBottom(){
        return this.bottom;
    }
}
