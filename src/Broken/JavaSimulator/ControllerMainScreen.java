package Broken.JavaSimulator;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

//import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerMainScreen implements Initializable{
    @FXML
    private StackPane root;
    private Label time = new Label("--:--");
    private Label day = new Label("--");
    private Label weatherToday = new Label("--");
    private Label weatherTomorrow = new Label("--");
    private HBox upHBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        double canvasWhith = (root.getPrefWidth()/7)*6;
        double canvasHeight = (root.getPrefHeight()/7)*6;
        Canvas map = new Canvas((int)canvasWhith,(int)canvasHeight);

        //main splitPane with Top and buttom panel
        SplitPane splitPane2 = new SplitPane();
        splitPane2.setOrientation(Orientation.VERTICAL);
        upHBox = new HBox(50);
        upHBox.setAlignment(Pos.CENTER);
        upHBox.setPrefWidth(root.getPrefWidth());

        //splitPane with map and right panel
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setPrefSize(root.getWidth(),root.getHeight());

        //Right splitPane with weather and player list
        SplitPane rightSplitPane = new SplitPane();
        rightSplitPane.setOrientation(Orientation.VERTICAL);


        VBox timeVbox = new VBox();
        timeVbox.setAlignment(Pos.CENTER);
        timeVbox.getChildren().addAll(new Label("Time"),time);
        VBox dayVbox = new VBox();
        dayVbox.setAlignment(Pos.CENTER);
        dayVbox.getChildren().addAll(new Label("Day"),day);



        TreeItem<String> rootPlayer = new TreeItem<>();
        TreeView<String> treeView = new TreeView<String>(rootPlayer);
        TreeItem<String> player1 = new TreeItem<>("Player1");
        player1.getChildren().addAll(new TreeItem<>("item1"),new TreeItem<>("Cash"),new TreeItem<>("etc..."));
        rootPlayer.getChildren().add(player1);
        treeView.setShowRoot(false);

        //Weather box
        VBox todayHbox = new VBox();
        todayHbox.setAlignment(Pos.CENTER);
        todayHbox.getChildren().addAll(new Label("Today:"),weatherToday);
        VBox tomorrowHbox = new VBox();
        tomorrowHbox.setAlignment(Pos.CENTER);
        tomorrowHbox.getChildren().addAll(new Label("Tomorrow"),weatherTomorrow);
        HBox weatherHBox = new HBox();
        weatherHBox.setAlignment(Pos.CENTER);
        weatherHBox.setSpacing(50);
        weatherHBox.getChildren().addAll(todayHbox,tomorrowHbox);
        weatherHBox.setStyle("-fx-font-weight: bold");

        rightSplitPane.getItems().addAll(weatherHBox,treeView);
        rightSplitPane.setDividerPositions(0.1);
        //fix divider position
        rightSplitPane.getDividers().get( 0 ).positionProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) ->  rightSplitPane.getDividers().get( 0 ).setPosition( 0.11 ));
        splitPane.getItems().addAll(map,rightSplitPane);





        upHBox.getChildren().addAll(timeVbox,dayVbox);
        upHBox.setStyle("-fx-font-size: 2em;-fx-font-weight: bold");
        splitPane2.getItems().addAll(upHBox,splitPane);
        root.getChildren().addAll(splitPane2);
    }
}
