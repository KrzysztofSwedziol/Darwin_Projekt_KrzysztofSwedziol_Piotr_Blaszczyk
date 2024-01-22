package agh.ics.oop.model;

import agh.ics.oop.presenter.ActionPresenter;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DarwinWorld implements Runnable{
    //private Map<Vector2d, Animal> animals = new HashMap<>();
    private boolean running = false;
    private ActionPresenter presenter;
    private HashMap<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private Jungle jungle;
    private int minimalReproduceEnergy;
    private int plantEnergy;
    private PoisonousArea poisonousArea;
    protected int width;
    protected int height;
    protected int ID;
    protected int day;
    private int initialAnimals;
    private int initialAnimalEnergy;
    private int genomeLength;
    private double reproduceEnergyUsage;
    private int mutationAmount;
    private int plantAmount;
    private int dailyPlants;
    private int area;


    public DarwinWorld(int width, int height, int ID, int plantAmount, int plantEnergy, int dailyPlants,
        int initialAnimals, int initialAnimalEnergy, int minimalReproduceEnergy, double reproduceEnergyUsage,
        int mutationAmount, int genomeLength){
        this.width = width;
        this.height = height;
        this.ID = ID;
        this.plantAmount = plantAmount;
        this.plantEnergy = plantEnergy;
        this.dailyPlants = dailyPlants;
        this.initialAnimals = initialAnimals;
        this. initialAnimalEnergy = initialAnimalEnergy;
        this.minimalReproduceEnergy = minimalReproduceEnergy;
        this.reproduceEnergyUsage = reproduceEnergyUsage;
        this.mutationAmount = mutationAmount;
        this.genomeLength = genomeLength;
        this.area = this.width*this.height;
        setPoisonousArea();
        setJungle();
        dailyPlantUpdate(plantAmount);
        randomPlaceAnimals(initialAnimals);

    }
    public void placePlant(Vector2d position, Plant plant){
        if(isOccupiedByPlant(position) == false) {
            plants.put(position, plant);
        }
    }
    public void placeAnimal(Vector2d position, Animal animal){
        if(isOccupiedByAnimal(position) == false){
            ArrayList<Animal> animalsAtPosition = new ArrayList<>();
            animalsAtPosition.add(animal);
            animals.put(position, animalsAtPosition);
        }if(isOccupiedByAnimal(position) == true){
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
        int side = (int)(Math.sqrt(0.2*area));
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
        //updateAnimalAge()
        Platform.runLater(()->{
            presenter.reDraw();
        });
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
            if(isOccupiedByPlant(position) == false){
                Plant plant = new Plant(position, false, plantEnergy);
                if(poisonousArea.doesFit(position)){
                    plant.setPoisonStatus(true);
                }
                this.plants.put(position, plant);
            }
        }else{
            Vector2d position = new Vector2d(randX, randY2);
            if(isOccupiedByPlant(position) == false){
                Plant plant = new Plant(position, false, plantEnergy);
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
                if(currAnimal.getPosition().getX() >= 0 && currAnimal.getPosition().getX() < this.width
                    && currAnimal.getPosition().getY() >= 0 && currAnimal.getPosition().getY() < this.height){

                    Vector2d newPosition = new Vector2d(currAnimal.getPosition().getX(),
                    currAnimal.getPosition().getY());

                    currAnimal.energy--;
                    if(tempAnimals.get(newPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(newPosition, animals2);
                    }else{
                        tempAnimals.get(newPosition).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getY() >= this.height &&
                currAnimal.getPosition().getX() >= this.width || currAnimal.getPosition().getY() < 0 &&
                currAnimal.getPosition().getX() >= this.width){
                    currAnimal.returnToPreviousPosition();
                    currAnimal.redoLastRotation();
                    currAnimal.rotate(3);
                    currAnimal.energy --;
                    if(tempAnimals.get(currAnimal.getPosition()) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currAnimal.getPosition(), animals2);
                    }else{
                        tempAnimals.get(currAnimal.getPosition()).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getY() >= this.height &&
                        currAnimal.getPosition().getX() < 0 || currAnimal.getPosition().getY() < 0 &&
                        currAnimal.getPosition().getX() < 0){
                    currAnimal.returnToPreviousPosition();
                    currAnimal.redoLastRotation();
                    currAnimal.rotate(3);
                    currAnimal.energy --;
                    if(tempAnimals.get(currAnimal.getPosition()) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currAnimal.getPosition(), animals2);
                    }else{
                        tempAnimals.get(currAnimal.getPosition()).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getY() >= this.height && currAnimal.getPosition().getX()>=0
                && currAnimal.getPosition().getX() < this.width||
                currAnimal.getPosition().getY() < 0 && currAnimal.getPosition().getX()>=0 &&
                currAnimal.getPosition().getX()<=this.width){
                    currAnimal.returnToPreviousPosition();
                    currAnimal.redoLastRotation();
                    currAnimal.rotate(3);
                    currAnimal.energy --;
                    if(tempAnimals.get(currAnimal.getPosition()) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(currAnimal.getPosition(), animals2);
                    }else{
                        tempAnimals.get(currAnimal.getPosition()).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getX() >= this.width &&
                   currAnimal.getPosition().getY() < this.height && currAnimal.getPosition().getY() >= 0) {
                    currAnimal.returnToPreviousPosition();
                    Vector2d newPosition = new Vector2d(0, currAnimal.getPosition().getY());
                    currAnimal.setPosition(newPosition);
                    currAnimal.energy--;
                    if(tempAnimals.get(newPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(newPosition, animals2);
                    }else{
                        tempAnimals.get(newPosition).add(currAnimal);
                    }
                }
                if(currAnimal.getPosition().getX() < 0 && currAnimal.getPosition().getY() < this.height &&
                   currAnimal.getPosition().getY() >= 0){
                    currAnimal.returnToPreviousPosition();
                    Vector2d newPosition = new Vector2d(width-1, currAnimal.getPosition().getY());
                    currAnimal.setPosition(newPosition);
                    currAnimal.energy--;
                    if(tempAnimals.get(newPosition) == null){
                        ArrayList<Animal> animals2 = new ArrayList<>();
                        animals2.add(currAnimal);
                        tempAnimals.put(newPosition, animals2);
                    }else{
                        tempAnimals.get(newPosition).add(currAnimal);
                    }
                }
            }
        }
        this.animals = tempAnimals;
    }
    public void removeDeadAnimals(){
        HashMap<Vector2d, ArrayList<Animal>> newAnimals = new HashMap<>();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> currList = entry.getValue();
            ArrayList<Animal> newSmallList = new ArrayList<>();
            for(Animal currAnimal : currList){
                if(currAnimal.getEnergy() > 0){
                    newSmallList.add(currAnimal);
                }
            }
            newAnimals.put(currPosition, newSmallList);
        }
        animals = newAnimals;

    }
    public void eatPlants(){
        HashMap<Vector2d, ArrayList<Animal>> tempAnimals = new HashMap<>();

        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2d currPosition = entry.getKey();
            ArrayList<Animal> aniList = entry.getValue();
            if(plants.get(currPosition) == null){
                updateAnimalsList(tempAnimals, aniList, currPosition);
            }
            if(plants.get(currPosition) != null && aniList.size() == 1) {
                Plant currPlant = plants.get(currPosition);
                Animal currAnimal = aniList.get(0);
                consume(currPlant, currAnimal, currPosition, aniList, tempAnimals);

            }
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
                    aniList.remove(currAnimal);
                    updateAnimalsList(tempAnimals, aniList, currPosition);
                    currAnimal.setPosition(potentialPosition);
                    if(tempAnimals.get(potentialPosition) != null){
                        tempAnimals.get(potentialPosition).add(currAnimal);
                    }else{
                        ArrayList<Animal> newSet = new ArrayList<>();
                        newSet.add(currAnimal);
                        tempAnimals.put(potentialPosition, newSet);
                    }
                }
                else{
                    potentialPosition = new Vector2d(plantX - 1, plantY);
                    aniList.remove(currAnimal);
                    updateAnimalsList(tempAnimals, aniList, currPosition);
                    currAnimal.setPosition(potentialPosition);
                    if(tempAnimals.get(potentialPosition) != null){
                        tempAnimals.get(potentialPosition).add(currAnimal);
                    }else{
                        ArrayList<Animal> newSet = new ArrayList<>();
                        newSet.add(currAnimal);
                        tempAnimals.put(potentialPosition, newSet);
                    }
                }

            }
        }
    }
    public void updateAnimalsList(HashMap<Vector2d, ArrayList<Animal>> tempAnimals,
    ArrayList<Animal> aniList, Vector2d position){
        if(tempAnimals.get(position) != null){
            for(Animal currAnimal : aniList){
                tempAnimals.get(position).add(currAnimal);
            }
        }else{
            ArrayList<Animal> newAniList = new ArrayList<>();
            for(Animal currAnimal : aniList){
                newAniList.add(currAnimal);
            }
            tempAnimals.put(position, newAniList);
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
                    ArrayList<Animal> currList = animals.get(position);
                    for(Animal currAnimal : currList){
                        result.append(currAnimal);
                    }
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
        if(x >= 0 && x < width && y >= 0 && y < height){
            return true;
        }
        return false;
    }
    public void reproduceAnimals(){
        for(Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()){
            Vector2d position = entry.getKey();
            ArrayList<Animal> aniList = entry.getValue();
            Animal animal1 = null;
            Animal animal2 = null;
            if(aniList.size() >= 3){
                animal1 = aniList.get(0);
                animal2 = aniList.get(1);
                aniList.sort((Animal a1, Animal a2) -> Integer.compare(a1.getEnergy(), a2.getEnergy()));
                if(aniList.get(1).getEnergy() > aniList.get(2).getEnergy()){
                    animal1 = aniList.get(0);
                    animal2 = aniList.get(1);
                }else{
                    aniList.sort((Animal a1, Animal a2) -> Integer.compare(a1.getAge(), a2.getAge()));
                    if(aniList.get(1).getAge() > aniList.get(2).getAge()){
                        animal1 = aniList.get(0);
                        animal2 = aniList.get(1);
                    }else{
                        aniList.sort((Animal a1, Animal a2) -> Integer.compare(a1.getKids(), a2.getKids()));
                        if(aniList.get(1).getKids() > aniList.get(2).getKids()){
                            animal1 = aniList.get(0);
                            animal2 = aniList.get(1);
                        }else{
                            Random rand = new Random();
                            int rand1 = rand.nextInt(aniList.size());
                            int rand2 = rand.nextInt(aniList.size());
                            while(rand1 == rand2){
                                rand2 = rand.nextInt(aniList.size());
                            }
                            animal1 = aniList.get(rand1);
                            animal2 = aniList.get(rand2);
                        }
                    }
                }
                mommyAndDaddyTime(animal1, animal2, position);
            }
            if(aniList.size() == 2){
                animal1 = aniList.get(0);
                animal2 = aniList.get(1);
                mommyAndDaddyTime(animal1, animal2, position);
            }
        }
    }

    public void mommyAndDaddyTime(Animal animal1, Animal animal2, Vector2d position){
        int energy1 = animal1.getEnergy();
        int energy2 = animal2.getEnergy();
        if(energy1 >= minimalReproduceEnergy && energy2 >= minimalReproduceEnergy){
            int sumEnergy = energy1 + energy2;
            double daddysPart = energy1/sumEnergy;
            double mommysPart = energy2/sumEnergy;
            int kidsEnergy = (int)(reproduceEnergyUsage*energy1 + reproduceEnergyUsage*energy2);
            animal1.setEnergy(energy1 - (int)(reproduceEnergyUsage*energy1));
            animal2.setEnergy(energy2 - (int)(reproduceEnergyUsage*energy2));
            int daddyGenomeLength = (int)(daddysPart*genomeLength);
            int mommyGenomeLength = (int)(mommysPart*genomeLength);
            Random randParrent = new Random();
            int choice = randParrent.nextInt(2) + 1;
            int[] kidGenome = new int[this.genomeLength];
            java.util.Arrays.fill(kidGenome, 0);
            if(choice == 1){
                for(int i=0; i<daddyGenomeLength; i++){
                    kidGenome[i] = animal1.getGenome()[i];
                }
                for(int i=0; i<mommyGenomeLength; i++){
                    kidGenome[i+daddyGenomeLength] = animal2.getGenome()[i+daddyGenomeLength];
                }
            }else{
                for(int i=0; i<mommyGenomeLength; i++){
                    kidGenome[i] = animal2.getGenome()[i];
                }
                for(int i=0; i<daddyGenomeLength; i++){
                    kidGenome[i+mommyGenomeLength] = animal1.getGenome()[i+mommyGenomeLength];
                }
            }
            mutateGenome(kidGenome);
            Animal kid = new Animal(new Vector2d(position.getX(), position.getY()), kidsEnergy, kidGenome);
            this.animals.get(position).add(kid);
        }
    }
    public void mutateGenome(int[] genome){
        Random rand = new Random();
        for(int i=0; i<mutationAmount; i++){
            int randomPosition = rand.nextInt(genomeLength);
            int randomGene = rand.nextInt(8);
            genome[randomPosition] = randomGene;
        }
    }
    public void randomPlaceAnimals(int amount){
        Random rand = new Random();
        for(int i=0; i<amount; i++){
            int randX = rand.nextInt(width);
            int randY = rand.nextInt(height);
            Vector2d randPosition = new Vector2d(randX, randY);
            int[] genome = new int[genomeLength];
            for(int z=0; z<genomeLength; z++){
                int randGene = rand.nextInt(8);
                genome[z] = randGene;
            }
            Animal animal = new Animal(randPosition, initialAnimalEnergy, genome);
            if(animals.get(randPosition) == null){
                ArrayList<Animal> aniList = new ArrayList<>();
                aniList.add(animal);
                animals.put(randPosition, aniList);
            }else{
                animals.get(randPosition).add(animal);
            }
        }
    }
    public void setPresenter(ActionPresenter presenter){
        this.presenter = presenter;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public HashMap<Vector2d, ArrayList<Animal>> getAnimals(){return this.animals;}
    public void run(){
        while(true){
            System.out.println("dupa");
            this.updateDay();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Wystąpił błąd podczas próby uśpienia wątku: " + e.getMessage());
            }
        }

    }
}
