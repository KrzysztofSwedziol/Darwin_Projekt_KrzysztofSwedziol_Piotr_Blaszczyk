package agh.ics.oop.model;
public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;

    public Animal(){
        this.position = new Vector2d(2,2);
        this.orientation = MapDirection.NORTH;
    }
    public Animal(Vector2d initialPosition){
        this.position = initialPosition;
        this.orientation = MapDirection.NORTH;
    }
    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case RIGHT:
                this.orientation = this.orientation.next();
                break;
            case LEFT:
                this.orientation = this.orientation.previous();
                break;
            case FORWARD:
                Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
                if (moveValidator.canMoveTo(newPosition)) {
                    this.position = newPosition;
                }
                break;
            case BACKWARD:
                Vector2d newPositionBack = this.position.subtract(this.orientation.toUnitVector());
                if (moveValidator.canMoveTo(newPositionBack)) {
                    this.position = newPositionBack;
                }
                break;
        }
    }
    @Override
    public String toString(){
        switch(orientation){
            case NORTH: return "N";
            case SOUTH: return "S";
            case WEST: return "W";
            case EAST: return "E";
            case NORTHEAST: return "NE";
            case SOUTHEAST: return "SE";
            case SOUTHWEST: return "SW";
            case NORTHWEST: return "NW";
            default: throw new IllegalArgumentException();
        }
    }
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public MapDirection getOrientation(){
        return this.orientation;
    }
    public Vector2d getPosition(){
        return this.position;
    }

}