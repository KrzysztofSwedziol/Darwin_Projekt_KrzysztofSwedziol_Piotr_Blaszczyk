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
import java.util.Map;  // Add this import
import java.util.HashMap;  // Add this import
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Plant;

import java.util.ArrayList;
import java.util.List;


public class ActionPresenter {
    private DarwinWorld world;
    private int pixelCellWidth;
    private int pixelCellHeight;
    private int days;

    public void setParameters(DarwinWorld world, int days){
        this.world = world;
        this.days = days;
        this.pixelCellWidth = (int)(750/(this.world.getWidth()+1));
        this.pixelCellHeight = (int)(750/(this.world.getHeight()+1));
    }

    public void drawMap(){

    }
    @FXML
    public void onSimulationStartClicked(){
        for(int i=0; i<this.days; i++){
            drawMap();
            this.world.updateDay();
        }
    }

}




/*<GridPane>
<columnConstraints>
<ColumnConstraints percentWidth="70" hgrow="SOMETIMES" minWidth="10.0" />
<ColumnConstraints percentWidth="30" hgrow="SOMETIMES" minWidth="10.0" />
</columnConstraints>
<children>
<GridPane GridPane.columnIndex="0" style="-fx-background-color: #0000ff;" minWidth="100" minHeight="100">

</GridPane>
<GridPane GridPane.columnIndex="1" style="-fx-background-color: black" minWidth="100" minHeight="100">

</GridPane>
</children>
</GridPane>*/
