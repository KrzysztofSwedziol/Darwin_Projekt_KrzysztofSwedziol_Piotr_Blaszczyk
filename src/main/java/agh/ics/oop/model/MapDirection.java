package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;


public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHEAST,
    SOUTHEAST,
    NORTHWEST,
    SOUTHWEST;


    /*@Override
    public String toString() {
        switch(this){
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case WEST: return "Zachód";
            case EAST: return "Wschód";
            default: throw new IllegalArgumentException();
        }
    }*/

    public MapDirection turn(int turns){
        MapDirection turned = this;
        for(int i=0; i<turns; i++){
            turned = this.next(turned);
        }
        return this;
    }
    public MapDirection turnBackwards(int turns){
        MapDirection turned = this;
        for(int i=0; i<turns; i++){
            turned = this.previous(turned);
        }
        return this;
    }
    public MapDirection next(MapDirection turned){
        switch(turned){
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case NORTHWEST: return NORTH;
            default: throw new IllegalArgumentException();
        }
    }
    public MapDirection previous(MapDirection turned){
        switch(turned){
            case NORTH: return NORTHWEST;
            case NORTHWEST: return WEST;
            case WEST: return SOUTHWEST;
            case SOUTHWEST: return SOUTH;
            case SOUTH: return SOUTHEAST;
            case SOUTHEAST: return EAST;
            case EAST: return NORTHEAST;
            case NORTHEAST: return NORTH;
            default: throw new IllegalArgumentException();
        }
    }
    public Vector2d toUnitVector(){
        switch(this){
            case NORTH: return new Vector2d(0, 1);
            case SOUTH: return new Vector2d(0, -1);
            case WEST: return new Vector2d(-1, 0);
            case EAST: return new Vector2d(1, 0);
            case NORTHEAST: return new Vector2d(1,1);
            case SOUTHEAST: return new Vector2d(1,-1);
            case SOUTHWEST: return new Vector2d(-1,-1);
            case NORTHWEST: return new Vector2d(-1,1);
            default: throw new IllegalArgumentException();
        }
    }
}