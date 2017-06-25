package Broken.JavaSimulator;

import Broken.JavaSimulator.GameUtils.Sale;
import Broken.JavaSimulator.Utils.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setMaximized(true);
        primaryStage.show();

    }


    public static void main(String[] args) throws IOException {
        Game game = new Game("https://webserverlemonade.herokuapp.com");
//        Game game = new Game("http://localhost:5000");
//        game.updateRegion();
        ArrayList<Sale> test = new ArrayList<>();
        test.add(new Sale("test","truc",10));
        test.add(new Sale("test1","truc1",101));
        test.add(new Sale("test2","truc2",1021));
        game.sendToServer(test);
        launch(args);
    }
}
