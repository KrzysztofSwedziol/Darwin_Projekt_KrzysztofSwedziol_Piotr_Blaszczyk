package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DarwinWorld {
    //private Map<Vector2d, Animal> animals = new HashMap<>();
    private HashMap<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private Jungle jungle;
    private PoisonousArea poisonousArea;
    protected int width;
    protected int height;
    protected int ID;
    protected int day;
    private int plantAmount;
    private int dailyPlants;
    private int area;

    public DarwinWorld(int width, int height, int ID, int plantAmount, int dailyPlants){
        this.width = width;
        this.height = height;
        this.ID = ID;
        this.plantAmount = plantAmount;
        this.dailyPlants = dailyPlants;
        this.area = this.width*this.height;
        setPoisonousArea();
        setJungle();
        dailyPlantUpdate(plantAmount);
    }
    public void placePlant(Vector2d position, Plant plant){
        if(isOccupiedByAnimal(position) == false && isOccupiedByPlant(position) == false) {
            plants.put(position, plant);
        }
    }
    public void placeAnimal(Vector2d position, Animal animal){
        if(isOccupiedByPlant(position) == false && isOccupiedByAnimal(position) == false){
            ArrayList<Animal> animalsAtPosition = new ArrayList<>();
            animalsAtPosition.add(animal);
            animals.put(position, animalsAtPosition);
        } if(isOccupiedByPlant(position) == true && isOccupiedByAnimal(position) == false) {
            plants.remove(position);
            ArrayList<Animal> animalsAtPosition = new ArrayList<>();
            animalsAtPosition.add(animal);
            animals.put(position, animalsAtPosition);
        }if(isOccupiedByPlant(position) == false && isOccupiedByAnimal(position) == true){
            animals.get(position).add(animal);
        }if(isOccupiedByPlant(position) == true && isOccupiedByAnimal(position) == true){
            plants.remove(position);
            animals.get(position).add(animal);
        }
    }
    public boolean isOccupiedByPlant(Vector2d position){
        if(plants.get(position) != null){
            return true;
        }
        return false;
    }
    public boolean isOccupiedByAnimal(Vector2d position){
        if(animals.get(position) != null){
            return true;
        }
        return false;
    }

    public void setJungle(){
        int jungleHeight = 0;
        jungleHeight = (int)((this.area/this.width)*0.2);
        int jungleY1 = (int)(this.height/2 - 0.5*jungleHeight);
        int jungleY2 = (int)(this.height/2 + 0.5*jungleHeight);
        Jungle equatorJungle = new Jungle(this.width, jungleY2 - jungleY1,
        new Coordinates(0, this.width, jungleY2, jungleY1), this, poisonousArea);
        this.jungle = equatorJungle;
    }
    public void setPoisonousArea(){
        int side = (int)(Math.sqrt(area));
        Coordinates coordinates = new Coordinates(0,side,side,0);
        PoisonousArea poisonousArea = new PoisonousArea(coordinates, side, side);
        this.poisonousArea = poisonousArea;
    }

    public void updateDay(){
        this.day++;
        dailyPlantUpdate(dailyPlants);
        removeDeadAnimals();
        moveAnimals();
        eatPlants();
        reproduceAnimals();
        updateAnimalAge();
    }
    public void dailyPlantUpdate(int plantAmount){
        Random rand = new Random();
        for(int i=0; i< plantAmount; i++){
            int randomNumber = rand.nextInt(10) + 1;
            if(randomNumber > 2){
                this.jungle.randomPlacePlant();
            }else{
                randomPlantSteppes();
            }
        }
    }
    public void randomPlantSteppes(){
        Random rand = new Random();
        int randX = rand.nextInt(this.width);
        int randY1 = rand.nextInt(this.jungle.getCoordinates().getBottom());
        int randY2 = rand.nextInt(this.height - this.jungle.getCoordinates().getCeiling()) +
        this.jungle.getCoordinates().getCeiling();
        int variant = rand.nextInt(2) + 1;
        if(variant == 1){
            Vector2d position = new Vector2d(randX, randY1);
            if(isOccupiedByPlant(position) == false && isOccupiedByAnimal(position)== false){
                Plant plant = new Plant(position, false);
                if(poisonousArea.doesFit(position)){
                    plant.setPoisonStatus(true);
                }
                this.plants.put(position, plant);
            }
        }else{
            Vector2d position = new Vector2d(randX, randY2);
            if(isOccupiedByPlant(position) == false && isOccupiedByAnimal(position)== false){
                Plant plant = new Plant(position, false);
                if(poisonousArea.doesFit(position)){
                    plant.setPoisonStatus(true);
                }
                this.plants.put(position, plant);
            }
        }
    }
    public void moveAnimals(){
        HashMap<Vector2d, ArrayList<Animal>> tempAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> animalList = entry.getValue();
            for(Animal currAnimal : animalList){
                currAnimal.move();
                if(currAnimal.getPosition().getY() > this.height ||
                        currAnimal.getPosition().getY() < 0){
                    currAnimal.returnToPreviousPosition();
                    currAnimal.redoLastRotation();
                    currAnimal.rotate(3);
                    currAnimal.energy --;
                    if(tempAnimals.get(currPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currPosition, animals2);
                    }else{
                        tempAnimals.get(currPosition).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getX() > this.width) {
                    currAnimal.returnToPreviousPosition();
                    Vector2d newPosition = new Vector2d(0, currAnimal.getPosition().getY());
                    currAnimal.setPosition(newPosition);
                    currAnimal.energy--;
                    if(tempAnimals.get(currPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currPosition, animals2);
                    }else{
                        tempAnimals.get(currPosition).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getX() < 0){
                    currAnimal.returnToPreviousPosition();
                    Vector2d newPosition = new Vector2d(width, currAnimal.getPosition().getY());
                    currAnimal.setPosition(newPosition);
                    currAnimal.energy--;
                    if(tempAnimals.get(currPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currPosition, animals2);
                    }else{
                        tempAnimals.get(currPosition).add(currAnimal);
                    }
                }else{
                    Vector2d newPosition = new Vector2d(currAnimal.getPosition().getX(),
                            currAnimal.getPosition().getY());
                    currAnimal.energy--;
                    if(tempAnimals.get(currPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currPosition, animals2);
                    }else{
                        tempAnimals.get(currPosition).add(currAnimal);
                    }
                }
            }
        }
        this.animals = tempAnimals;
    }
    public void removeDeadAnimals(){
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> currList = entry.getValue();
            for(Animal currAnimal : currList){
                if(currAnimal.getEnergy() <= 0){
                    animals.get(currPosition).remove(currAnimal);
                    if(animals.get(currPosition).size() == 0){
                        animals.remove(currPosition);
                    }
                }
            }
        }

    }
    public void eatPlants(){
        HashMap<Vector2d, ArrayList<Animal>> tempAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> aniList = entry.getValue();
            if(plants.get(currPosition) != null && aniList.size() > 1){
                aniList.sort((Animal a1, Animal a2) -> Integer.compare(a1.getEnergy(), a2.getEnergy()));
                Plant currPlant = plants.get(currPosition);
                Animal currAnimal = aniList.get(0);
                if(aniList.get(0).getEnergy() > aniList.get(1).getEnergy()){
                    currAnimal = aniList.get(0);
                }else{
                    aniList.sort((Animal a1, Animal a2) -> Integer.compare(a2.getAge(), a1.getAge()));
                    if(aniList.get(0).getAge() > aniList.get(1).getAge()){
                        currAnimal = aniList.get(0);
                    }else{
                        aniList.sort((Animal a1, Animal a2) -> Integer.compare(a2.getKids(), a1.getKids()));
                        if(aniList.get(0).getKids() > aniList.get(1).getKids()){
                            currAnimal = aniList.get(0);
                        }else{
                            Random rand = new Random();
                            int index = rand.nextInt(aniList.size());
                            currAnimal = aniList.get(index);
                        }
                    }
                }
                consume(currPlant, currAnimal, currPosition,aniList, tempAnimals);
            }if(plants.get(currPosition) != null && aniList.size() == 1) {
                Plant currPlant = plants.get(currPosition);
                Animal currAnimal = aniList.get(0);
                consume(currPlant, currAnimal, currPosition, aniList, tempAnimals);
            }
        }
        this.animals = tempAnimals;
    }
    public void consume(Plant currPlant, Animal currAnimal, Vector2d currPosition,
                        ArrayList<Animal> aniList, HashMap<Vector2d, ArrayList<Animal>> tempAnimals){
        if(currPlant.isPoisonous() == false){
            int energyToAdd = currPlant.getEnergy();
            int animalEnergy = currAnimal.getEnergy();
            currAnimal.setEnergy(animalEnergy + energyToAdd);
            plants.remove(currPosition);
            updateAnimalsList(tempAnimals, aniList, currPosition);

        }else{
            int energyToAdd = currPlant.getEnergy();
            int animalEnergy = currAnimal.getEnergy();
            Random observanceTest = new Random();
            int result = observanceTest.nextInt(10)+1;
            if(result > 2){
                currAnimal.setEnergy(animalEnergy + energyToAdd);
                plants.remove(currPosition);
                updateAnimalsList(tempAnimals, aniList, currPosition);
            }else{
                Vector2d plantPosition = currPlant.getPosition();
                int plantX = plantPosition.getX();
                int plantY = plantPosition.getY();
                Vector2d potentialPosition = new Vector2d(plantX + 1, plantY);
                if(fitsToMap(potentialPosition)){
                    aniList.remove(currPosition);
                    currAnimal.setPosition(potentialPosition);
                    if(tempAnimals.get(potentialPosition) != null){
                        tempAnimals.get(potentialPosition).add(currAnimal);
                    }else{
                        ArrayList<Animal> newSet = new ArrayList<>();
                        newSet.add(currAnimal);
                        tempAnimals.put(potentialPosition, newSet);
                    }
                    updateAnimalsList(tempAnimals, aniList, currPosition);
                }
                else{
                    potentialPosition = new Vector2d(plantX - 1, plantY);
                    aniList.remove(currPosition);
                    currAnimal.setPosition(potentialPosition);
                    if(tempAnimals.get(potentialPosition) != null){
                        tempAnimals.get(potentialPosition).add(currAnimal);
                    }else{
                        ArrayList<Animal> newSet = new ArrayList<>();
                        newSet.add(currAnimal);
                        tempAnimals.put(potentialPosition, newSet);
                    }
                    updateAnimalsList(tempAnimals, aniList, currPosition);
                }

            }
        }
    }
    public void updateAnimalsList(HashMap<Vector2d, ArrayList<Animal>> tempAnimals,
    ArrayList<Animal> aniList, Vector2d position){
        if(tempAnimals.get(position) != null){
            for(int i=0; i<aniList.size(); i++){
                Animal animal = aniList.get(i);
                tempAnimals.get(position).add(animal);
            }
        }else{
            tempAnimals.put(position, aniList);
        }
    }
    public void updateAnimalAge(){
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()){
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> aniList = entry.getValue();
            for(Animal animal : aniList){
                animal.updateAge();
            }
        }
    }

    public Map<Vector2d, Plant> getPlants(){
        return plants;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                Vector2d position = new Vector2d(x, y);

                if (isOccupiedByAnimal(position)) {
                    result.append("A ");
                } else if (isOccupiedByPlant(position)) {
                    result.append("* ");
                } else {
                    result.append("| ");
                }

                // Add space between cells
                result.append(" ");
            }
            result.append("\n"); // Move to the next line
        }

        return result.toString();
    }
    public boolean fitsToMap(Vector2d position){
        int x = position.getX();
        int y = position.getY();
        if(x >= 0 && x <= width && y >= 0 && y <= height){
            return true;
        }
        return false;
    }
    public void reproduceAnimals(){

    }
}
