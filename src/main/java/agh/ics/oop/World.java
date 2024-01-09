package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.DarwinWorld;
import agh.ics.oop.model.Vector2d;

public class World {
    public static void main(String[] args) {
        DarwinWorld newWorld = new DarwinWorld(10, 10, 0, 30, 2,
                10, 5, 20, 1, 0.3, 3, 10);
        Simulation simulation = new Simulation(newWorld, 8);
        simulation.run();
    }
}
