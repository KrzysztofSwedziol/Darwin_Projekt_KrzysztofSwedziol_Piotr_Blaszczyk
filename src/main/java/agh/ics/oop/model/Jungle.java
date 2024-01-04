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
    public void setPlants(){
        Random rand1 = new Random();
        double random = 0.3 + (0.6 - 0.3) * rand1.nextDouble(); //losowanie z zakresu 0,3-0,6 procentu
        //obszaru jaki będą stanowić rośliny.
        int plantsToSet = (int)(random*area) - adjustToOwnerPlants();
        int counter = 0;
        if(plantsToSet > 0){
            while(counter <= plantsToSet){
                Random rand2 = new Random();
                int randWidth = rand2.nextInt(width);
                int randHeight = rand2.nextInt(height);
                int x = coordinates.getLeftSide() + randWidth;
                int y = coordinates.getBottom() + randHeight;
                Vector2d position = new Vector2d(x, y);
                if(plants.get(position) == null && owner.isOccupiedByPlant(position) == false &&
                        owner.isOccupiedByAnimal(position) == false){
                    Plant plant = new Plant(position);
                    plants.put(position, plant);
                    owner.placePlant(position, plant);
                    counter++;
                }

            }
        }
    }
    public int adjustToOwnerPlants(){
        int counter = 0;
        for(Map.Entry<Vector2d, Plant> entry : this.owner.getPlants().entrySet()){
            if(fitsToMap(entry.getKey())){
                if(plantInJungle(entry.getValue())){
                    counter ++;
                }else{
                    plants.put(entry.getKey(), entry.getValue());
                    counter++;
                }
            }
        }
        return counter;
    }
    public void removePlant(Vector2d position){
        plants.remove(position);
    }
    public Coordinates getCoordinates(){
        return this.coordinates;
    }
    public void placePlant(Vector2d position, Plant plant){
        if(plants.get(position) == null){
            plants.put(position, plant);
        }
    }
    public boolean plantInJungle(Plant plant){
        if(plants.get(plant.getPosition()) != null){
            return true;
        }
        return false;
    }
    public boolean fitsToMap(Vector2d position){
        if(position.getX() >= this.coordinates.getLeftSide() &&
        position.getX() <= this.coordinates.getRightSide() &&
        position.getY() >= this.coordinates.getBottom() &&
        position.getY() <= this.coordinates.getCeiling()){
            return true;
        }
        return false;
    }

}
