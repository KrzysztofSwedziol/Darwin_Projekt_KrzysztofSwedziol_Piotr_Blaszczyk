package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.DarwinWorld;

public class World {
    public static void main(String[] args) {
        DarwinWorld newWorld = new DarwinWorld(10, 10, 0, 20, 10);
        Simulation simulation = new Simulation(newWorld, 5);
        simulation.run();
    }
}
