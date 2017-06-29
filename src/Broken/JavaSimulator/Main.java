package Broken.JavaSimulator;

import Broken.JavaSimulator.GameUtils.Sale;
import Broken.JavaSimulator.Utils.Game;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;



public class Main extends Application {
    public static Game game = new Game("http://webserverlemonade.herokuapp.com");
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Lemonade Simulation !");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setMaximized(true);
        primaryStage.show();

    }




    public static void main(String[] args) throws IOException {
//        Game game = new Game("http://localhost:5000");
        game.updateRegion();
        game.updateTime();
        launch(args);
    }
}
