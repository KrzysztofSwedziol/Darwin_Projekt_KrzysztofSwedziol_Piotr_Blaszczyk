package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;  // Add this import
import java.util.HashMap;  // Add this import

import java.util.ArrayList;
import java.util.List;

public class SimulationPresenter {
    private DarwinWorld map;

    @FXML
    private GridPane mapGrid;

    @FXML
    private GridPane controlCenter;

    @FXML
    private TextField widthContainer;

    @FXML
    private TextField heightContainer;

    @FXML
    private TextField IDContainer;

    @FXML
    private TextField plantContainer;

    @FXML
    private TextField plantEnergyContainer;

    @FXML
    private TextField dailyPlantsContainer;

    @FXML
    private TextField initialAnimalsContainer;

    @FXML
    private TextField initialAnimalEnergyContainer;

    @FXML
    private TextField reproduceEnergyContainer;

    @FXML
    private TextField reproduceEnergyUsageContainer;

    @FXML
    private TextField mutationAmountContainer;

    @FXML
    private TextField genomeLengthContainer;

    @FXML
    private TextField daysAmountContainer;

    private static final int CELL_WIDTH = 45;
    private static final int CELL_HEIGHT = 45;

    public void setWorldMap(DarwinWorld map) {

        this.map = map;
    }
    @FXML
    public void onSimulationStartClicked() {
        int width = Integer.parseInt(widthContainer.getText());
        int height = Integer.parseInt(heightContainer.getText());
        int ID = Integer.parseInt(IDContainer.getText());
        int plantAmount = Integer.parseInt(plantContainer.getText());
        int plantEnergy = Integer.parseInt(plantEnergyContainer.getText());
        int dailyPlants = Integer.parseInt(dailyPlantsContainer.getText());
        int initialAnimals = Integer.parseInt(initialAnimalsContainer.getText());
        int initialAnimalEnergy = Integer.parseInt(initialAnimalEnergyContainer.getText());
        int reproduceEnergy = Integer.parseInt(reproduceEnergyContainer.getText());
        int reproduceEnergyUsage = Integer.parseInt(reproduceEnergyUsageContainer.getText());
        int mutationAmount = Integer.parseInt(mutationAmountContainer.getText());
        int genomeLength = Integer.parseInt(genomeLengthContainer.getText());
        int daysAmount = Integer.parseInt(daysAmountContainer.getText());

        DarwinWorld darwinWorld = new DarwinWorld(width, height, ID, plantAmount, plantEnergy, dailyPlants
                , initialAnimals, initialAnimalEnergy, reproduceEnergy, reproduceEnergyUsage, mutationAmount
                , genomeLength);
        setWorldMap(darwinWorld);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("action.fxml"));
        Stage newStage = new Stage();
        try {
            Parent root = loader.load();
            newStage.setTitle("Darwin");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((ActionPresenter)loader.getController()).setParameters(this.map, daysAmount);

    }
}



    /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("action.fxml"));
                Stage newStage = new Stage();
                try {
                Parent root = loader.load();
                newStage.setTitle("Darwin");
                newStage.setScene(new Scene(root));
                newStage.setResizable(false);
                newStage.show();
                } catch (IOException e) {
                throw new RuntimeException(e);
                }
                ((ActionPresenter)loader.getController()).setParameters(this.map, daysAmount);*/
