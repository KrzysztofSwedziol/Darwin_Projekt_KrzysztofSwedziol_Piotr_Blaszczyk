package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }
    public boolean precedes(Vector2d other){
        if(other.x <= this.x && other.y <= this.y){
            return true;
        }
        return false;
    }
    public boolean follows(Vector2d other){
        if(other.x >= this.x && other.y >= this.y){
            return true;
        }
        return false;
    }

    public Vector2d add(Vector2d other){
        Vector2d new_vector = new Vector2d(this.x + other.getX(), this.y + other.getY());
        return new_vector;

    }
    public Vector2d subtract(Vector2d other){
        Vector2d new_vector = new Vector2d(this.x - other.x, this.y-other.y);
        return new_vector;
    }


    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(other.x, this.x), Math.max(other.y, this.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(other.x, this.x), Math.min(other.y, this.y));
    }
    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
