package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class AnimalTest {

    @Test
    public void orientationTest1() {
        Animal animal = new Animal();
        animal.rotate(2);
        MapDirection orientation = MapDirection.EAST;
        assertEquals(animal.getOrientation(), orientation );
    }

    @Test
    public void orientationTest2() {
        Animal animal = new Animal();
        animal.rotate(0);
        MapDirection orientation = MapDirection.NORTH;
        assertEquals(animal.getOrientation(), orientation );
    }

    @Test
    public void orientationTest3() {
        Animal animal = new Animal();
        animal.rotate(8);
        MapDirection orientation = MapDirection.NORTH;
        assertEquals(animal.getOrientation(), orientation );
    }

    @Test
    public void positionTest1() {
        Animal animal = new Animal();

        Vector2d hint = new Vector2d(2,2);
        assertEquals(animal.getPosition(), hint );
    }

    @Test
    public void positionTest2() {
        Animal animal = new Animal();

        int[] genom = {0};
        animal.setGenome(genom);
        animal.move();

        Vector2d hint = new Vector2d(2,3);
        assertEquals(animal.getPosition(), hint );
    }

    @Test
    public void positionTest3() {
        Animal animal = new Animal();

        int[] genom = {0, 0, 0};
        animal.setGenome(genom);
        animal.move();
        animal.move();
        animal.move();

        Vector2d hint = new Vector2d(2,5);
        assertEquals(animal.getPosition(), hint );
    }

    @Test
    public void positionTest4() {
        Animal animal = new Animal();

        int[] genom = {0, 1, 1};
        animal.setGenome(genom);
        animal.move();
        animal.move();
        animal.move();

        Vector2d hint = new Vector2d(4,4);
        assertEquals(animal.getPosition(), hint );
    }

    @Test
    public void positionTest7() {
        Animal animal = new Animal();

        int[] genom = {0, 4, 5, 2, 4, 0, 5, 3, 6, 5, 7, 1, 3, 5, 0, 7, 1, 2, 0, 5, 3, 7, 1, 2, 4, 3,
                1, 3, 2, 3, 1, 3, 5, 0, 3, 6, 0, 7, 0, 5, 0, 1, 7, 5, 4, 2, 0, 0,
                2, 1, 6, 0, 6, 1, 2, 5, 5, 4, 2, 0, 5, 0, 6, 3, 5, 2, 4, 6, 5, 0, 1, 5, 1, 2, 4,
                7, 6, 0, 3, 4, 7, 2, 4, 6, 5, 3, 0, 6, 6, 6, 0, 2, 3, 4, 2, 3, 3, 5, 0, 2};
        animal.setGenome(genom);
        for (int i = 0; i< genom.length; i++) {
            animal.move();
        }
        //tylko w mapie jest test czy nie wychodzi poza mape
        Vector2d hint = new Vector2d(-1,11);
        assertEquals(animal.getPosition(), hint );
    }

}
