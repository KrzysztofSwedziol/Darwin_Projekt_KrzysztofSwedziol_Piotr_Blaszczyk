package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.DarwinWorld;

public class World {
    public static void main(String[] args) {
        DarwinWorld newWorld = new DarwinWorld(30, 15, 0, 5, 10);
        System.out.println(newWorld);
    }
}
