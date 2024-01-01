package agh.ics.oop.model;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Jungle {
    private int width;
    private int height;
    private Coordinates coordinates;
    private int area;
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private DarwinWorld owner; //world that owns this jungle

    public Jungle(int width, int height, Coordinates coordinates, DarwinWorld owner){
        this.width = width;
        this.height = height;
        this.area = width*height;
        this.coordinates = coordinates;
        this.owner = owner;
        setPlants();
    }
    private void setPlants(){
        Random rand = new Random();
        double random = 0.3 + (0.6 - 0.3) * rand.nextDouble(); //losowanie z zakresu 0,3-0,6 procentu
        //obszaru jaki będą stanowić rośliny.
        int plantsToSet = (int)(random*area);
        int counter = 0;
        while(counter <= plantsToSet){
            int randWidth = rand.nextInt(width);
            int randHeight = rand.nextInt(height);
            int x = coordinates.getLeftSide() + randWidth;
            int y = coordinates.getBottom() + randHeight;
            Vector2d position = new Vector2d(x, y);
            if(plants.get(position) == null){
                Plant plant = new Plant(position);
                plants.put(position, plant);
                owner.placePlant(position, plant);
                counter++;
            }

        }

    }
}
