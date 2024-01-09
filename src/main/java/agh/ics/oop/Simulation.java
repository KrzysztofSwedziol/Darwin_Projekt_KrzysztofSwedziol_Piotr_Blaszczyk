package agh.ics.oop;
import java.util.List;
import agh.ics.oop.model.*;
import java.util.ArrayList;

public class Simulation  implements Runnable {
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

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Wystąpił błąd podczas próby uśpienia wątku: " + e.getMessage());
            }
        }

    }



}
