package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.DarwinWorld;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.awt.*;
import java.util.Map;  // Add this import
import java.util.HashMap;  // Add this import
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Plant;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.Node;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ActionPresenter {
    private DarwinWorld world;
    private int pixelCellWidth;
    private int pixelCellHeight;
    private int days;
    @FXML
    private Label animals;
    @FXML
    private Label plants;
    @FXML
    private Label day;
    @FXML
    private GridPane mapGrid;
    private Image grassImage = new Image("photos/grass.png");
    private Image healthyFruitImage = new Image("photos/healthyFruit.png");
    private Image unhealthyFruitImage = new Image("photos/unhealthyFruit.png");
    private Image animalNimage = new Image("photos/animalN.png");
    private Image animalNEimage = new Image("photos/animalNE.png");
    private Image animalEimage = new Image("photos/animalE.png");
    private Image animalSEimage = new Image("photos/animalSE.png");
    private Image animalSimage = new Image("photos/animalS.png");
    private Image animalSWimage = new Image("photos/animalSW.png");
    private Image animalWimage = new Image("photos/animalW.png");
    private Image animalNWimage = new Image("photos/animalNW.png");
    public void setParameters(DarwinWorld world, int days){
        this.world = world;
        this.days = days;
        this.pixelCellWidth = (int)(750/(this.world.getWidth()+1));
        this.pixelCellHeight = (int)(750/(this.world.getHeight()+1));
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public Canvas setCanvas(Image image){
        Canvas canvas = new Canvas(this.pixelCellWidth-1, this.pixelCellHeight-1);
        GraphicsContext container = canvas.getGraphicsContext2D();
        //Image image = new Image(path);
        container.drawImage(image, 0, 0, this.pixelCellWidth, this.pixelCellHeight);
        container = null;
        System.gc();
        return canvas;
    }
    public void drawMap(){
        Label topLeftLabel = new Label("Y/X");
        mapGrid.add(topLeftLabel, 0, 0);
        topLeftLabel = null;
        System.gc();

        // Display numeric indices in the first row
        for (int i = 1; i < this.world.getWidth()+1; i++) {
            Label label = new Label(String.valueOf(i));
            mapGrid.add(label, i, 0);
            label = null;
            System.gc();
        }

        // Display numeric indices in the first column
        for (int i = 1; i < this.world.getHeight()+1; i++) {
            Label label = new Label(String.valueOf(i));
            mapGrid.add(label, 0, i);
            label = null;
            System.gc();
        }

        // Add lines to divide the cells
        for (int j = 0; j <= this.world.getHeight(); j++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(this.pixelCellHeight);
            mapGrid.getRowConstraints().add(rowConstraints);
            rowConstraints = null;
            System.gc();
        }
        for (int i = 0; i <= this.world.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(this.pixelCellWidth);
            mapGrid.getColumnConstraints().add(columnConstraints);
            columnConstraints = null;
            System.gc();
        }

        for(int i=0; i<this.world.getWidth(); i++){
            for(int j=0; j<this.world.getHeight(); j++){
                Vector2d currPosition = new Vector2d(i, j);
                //Canvas canvas = setCanvas("photos/grass.png");
                Canvas canvas = setCanvas(grassImage);
                mapGrid.add(canvas, i+1, j+1);
                canvas = null;
                System.gc();
                if(this.world.getPlants().get(currPosition) != null){
                    Plant currPlant = this.world.getPlants().get(currPosition);
                    if(currPlant.isPoisonous() == false){
                        canvas = setCanvas(healthyFruitImage);
                        mapGrid.add(canvas, i+1, j+1);
                        canvas = null;
                        System.gc();
                    }else{
                        canvas = setCanvas(unhealthyFruitImage);
                        mapGrid.add(canvas, i+1, j+1);
                        canvas = null;
                        System.gc();
                    }

                }else if(this.world.getAnimals().get(currPosition) != null){
                    Animal animal = this.world.getAnimals().get(currPosition).get(0);
                    String orientation = animal.toString();
                    Image image = null;
                    switch (orientation) {
                        case "N" -> {
                            image = animalNimage;
                        }
                        case "NE" -> {
                            image = animalNEimage;
                        }
                        case "E" -> {
                            image = animalEimage;
                        }
                        case "SE" -> {
                            image = animalSEimage;
                        }
                        case "S" -> {
                            image = animalSimage;
                        }
                        case "SW" -> {
                            image = animalSWimage;
                        }
                        case "W" -> {
                            image = animalWimage;
                        }
                        case "NW" -> {
                            image = animalNWimage;
                        }
                    }
                    canvas = setCanvas(image);
                    mapGrid.add(canvas, i+1, j+1);
                    canvas = null;
                    System.gc();
                }
            }
        }
    }
    @FXML
    public void onSimulationStartClicked(){
        /*for(int i=0; i<this.days; i++){
                Platform.runLater(() -> {
                    this.world.run();
                    clearGrid();
                    drawMap();
                });
            animals.setText("Animals: " + this.world.getAnimals().size());
            plants.setText("Plants: " + this.world.getPlants().size());
            day.setText("Day: " + (i+1));
        }*/
        this.world.setPresenter(this);
        Thread worldThread = new Thread(this.world::run);
        worldThread.start();
    }
    public void reDraw(){
        clearGrid();
        drawMap();
    }
}
