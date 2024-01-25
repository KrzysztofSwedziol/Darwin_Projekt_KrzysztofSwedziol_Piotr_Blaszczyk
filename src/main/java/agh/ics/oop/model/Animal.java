package agh.ics.oop.model;
public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private Vector2d previousPosition;
    public int energy;
    private int age;
    private int kidsAmount;
    private int[] genome;
    private int genomeLength;
    private int currGen;
    private int variant;
    private boolean whereToStart;
    //if its true then we move according to genome from left to right
    //and when its false we go from right to left

    public Animal(){
        this.position = new Vector2d(2,2);
        this.orientation = MapDirection.NORTH;
        this.currGen = 0;
    }
    public Animal(Vector2d initialPosition, int energy, int[] genome, int variant){
        this.position = initialPosition;
        this.orientation = MapDirection.NORTH;
        this.energy = energy;
        this.genome = genome;
        this.genomeLength = genome.length;
        this.currGen = 0;
        this.age = 0;
        this.kidsAmount = 0;
        this.whereToStart = true;
        this.variant = variant;
    }
    public void rotate(int rotates){
        this.orientation = this.orientation.turn(rotates);
    }
    public void move(){
        if(variant == 2){
            if(whereToStart == true){
                moveLeftToRight();
            }else{
                moveRightToLeft();
            }
        }else{
            moveNormal();
        }
    }
    public void moveNormal(){
        if(currGen == genomeLength){
            currGen = 0;
        }
        rotate(this.genome[this.currGen]);
        Vector2d currMove = this.orientation.toUnitVector();
        this.previousPosition = new Vector2d(position.getX(), position.getY());
        this.position = this.position.add(currMove);
        this.currGen++;
    }
    public void moveLeftToRight(){
        if(currGen == genomeLength){
            currGen = genomeLength - 1;
            this.whereToStart = false;
            move();
        }else{
            rotate(this.genome[this.currGen]);
            Vector2d currMove = this.orientation.toUnitVector();
            this.previousPosition = new Vector2d(position.getX(), position.getY());
            this.position = this.position.add(currMove);
            this.currGen++;
        }
    }
    public void moveRightToLeft(){
        if(currGen == -1){
            currGen = 0;
            this.whereToStart = true;
            move();
        }else{
            rotate(this.genome[this.currGen]);
            Vector2d currMove = this.orientation.toUnitVector();
            this.previousPosition = new Vector2d(position.getX(), position.getY());
            this.position = this.position.add(currMove);
            this.currGen--;
        }

    }
    public Vector2d getPosition(){
        return this.position;
    }
    public Vector2d getPreviousPosition(){
        return this.previousPosition;
    }
    public void returnToPreviousPosition(){
        Vector2d positionCopy = new Vector2d(position.getX(), position.getY());
        this.position = previousPosition;
        previousPosition = positionCopy;
    }
    public int[] getGenome(){
        return this.genome;
    }
    public int getCurrGen(){
        return this.currGen;
    }
    public void redoLastRotation(){
        if(this.whereToStart == true){
            this.currGen--;
            this.orientation = this.orientation.turnBackwards(this.genome[this.currGen]);
        }
        if(this.whereToStart == false){
            this.currGen++;
            this.orientation = this.orientation.turnBackwards(this.genome[this.currGen]);
        }
    }
    public void setPosition(Vector2d position){
        this.position = position;
    }
    public int getEnergy(){
        return this.energy;
    }
    public int getAge(){
        return this.age;
    }
    public int getKids(){
        return this.kidsAmount;
    }
    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
    }
    public void updateAge(){
        this.age++;
    }


    @Override
    public String toString() {
        switch (this.orientation) {
            case NORTH:
                return "N";
            case NORTHEAST:
                return "NE";
            case EAST:
                return "E";
            case SOUTHEAST:
                return "SE";
            case SOUTH:
                return "S";
            case SOUTHWEST:
                return "SW";
            case WEST:
                return "W";
            case NORTHWEST:
                return "NW";
            default:
                return "";
        }
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public void setGenome(int[] newGenome) {
        this.genome = new int[newGenome.length];
        System.arraycopy(newGenome, 0, this.genome, 0, newGenome.length);
        this.genomeLength = newGenome.length;
        this.currGen = 0;
    }

}