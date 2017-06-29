package Broken.JavaSimulator.resources;

import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by sebastien on 29/06/17.
 */
public class Ranking implements Initializable{
    @FXML
    Button refresh;

    @FXML
    ListView list;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Player> players = Main.game.getRegion().getPlayers();

        int i = 1;
        for(Player aPlayer : players){
            VBox vBox = new VBox(1);
            if(i == 1)
            {
                vBox.setStyle("-fx-font-weight: bold;-fx-font-size: 140% ");
            }
            else
                vBox.setStyle("-fx-font-size: 120% ");
            //vBox.setStyle("-fx-font: bold");
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(new Label(String.valueOf(i)),new Label(aPlayer.getID()),new Label("Cash: "+aPlayer.getCash()+"€"));
            list.getItems().add(vBox);
            i++;
        }
    }
}
