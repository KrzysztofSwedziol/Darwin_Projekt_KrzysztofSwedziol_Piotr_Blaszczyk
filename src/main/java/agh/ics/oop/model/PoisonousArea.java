package agh.ics.oop.model;

public class PoisonousArea {
    private Coordinates coordinates;
    private int width;
    private int height;
    private int area;

    public PoisonousArea(Coordinates coordinates, int width, int height){
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
    }
    public boolean doesFit(Vector2d position){
        return(position.getX() >= coordinates.getLeftSide() && position.getX() <= coordinates.getRightSide()
        && position.getY() >= coordinates.getBottom() && position.getY() <= coordinates.getCeiling());

    }
}
