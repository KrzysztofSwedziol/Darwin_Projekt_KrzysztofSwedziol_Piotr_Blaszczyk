package agh.ics.oop;
import java.util.List;
import agh.ics.oop.model.*;
import java.util.ArrayList;

public class Simulation  implements Runnable {
    private List<MoveDirection> directions;
    private DarwinWorld map;
    private int days;
    public Simulation(DarwinWorld map, int days) {
        this.map = map;
        this.days = days;
    }
    public void run(){
        for(int i=0; i<this.days; i++){
            System.out.println(map);
            map.updateDay();
        }

    }



}
