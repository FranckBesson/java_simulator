package Broken.JavaSimulator;

import Broken.JavaSimulator.GameUtils.*;
import Broken.JavaSimulator.Utils.ConvertPoss;
import Broken.JavaSimulator.Utils.TreeViewUtils;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ControllerMainScreen implements Initializable{
    @FXML
    private StackPane root;
    private Label time = new Label("--:--");
    private Label day = new Label("--");
    private Label weatherToday = new Label("--");
    private Label weatherTomorrow = new Label("--");
    private HBox upHBox;
    private SplitPane splitPane2;
    private Canvas map;
    private SplitPane splitPane;
    private SplitPane rightSplitPane;
    private VBox timeVbox;
    private VBox dayVbox;
    private TreeView<String> treeView;
    private float circleSize = 7;

    private Boolean first = true;
    private String selectedPlayer = "null";


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        double canvasWhith = (root.getPrefWidth()/5)*4;
        double canvasHeight = (root.getPrefHeight()/5)*4;
        map = new Canvas((int)canvasWhith,(int)canvasHeight);

        //main splitPane with Top and buttom panel
        splitPane2 = new SplitPane();
        splitPane2.setOrientation(Orientation.VERTICAL);
        upHBox = new HBox(50);
        upHBox.setAlignment(Pos.CENTER);
        upHBox.setPrefWidth(root.getPrefWidth());


        //splitPane with map and right panel
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setPrefSize(root.getWidth(),root.getHeight());

        //Right splitPane with weather and player list
        rightSplitPane = new SplitPane();
        rightSplitPane.setOrientation(Orientation.VERTICAL);


        timeVbox = new VBox();
        timeVbox.setAlignment(Pos.CENTER);
        timeVbox.getChildren().addAll(new Label("Time"),time);
        dayVbox = new VBox();
        dayVbox.setAlignment(Pos.CENTER);
        dayVbox.getChildren().addAll(new Label("Day"),day);


        //Treeview on right
        TreeItem<String> rootPlayer = new TreeItem<>();
        treeView = new TreeView<String>(rootPlayer);
        treeView.setShowRoot(false);
        treeView.setStyle("-fx-font-weight: bold");
        treeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> c) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItems().get(0);
                selectedPlayer = "null";

                for(Player aplayer : Main.game.getRegion().getPlayers()){
                    if(aplayer.getID().equals(selectedItem.getValue())){
                        selectedPlayer = aplayer.getID();
                    }
                }
                updateCanvas();
            }
        });

        //Weather box
        VBox todayHbox = new VBox();
        todayHbox.setAlignment(Pos.CENTER);
        todayHbox.getChildren().addAll(new Label("Today"),weatherToday);
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
        splitPane2.getDividers().get( 0 ).positionProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) ->  splitPane2.getDividers().get( 0 ).setPosition( 0.11 ));

        root.getChildren().addAll(splitPane2);

        splitPane2.getDividers().get(0).setPosition(0);
        new StabWait().start();
        update();

    }

    public void updateScreenSize(){
        //System.out.println(root.getWidth());
        double canvasWhith = (root.getWidth()/5)*4;
        double canvasHeight = (root.getHeight()/5)*4;
        map.setHeight(canvasHeight);
        map.setWidth(canvasWhith);
        splitPane.setPrefSize(root.getWidth(),root.getHeight());
        splitPane.getDividers().get(0).setPosition(0);
        splitPane2.getDividers().get(0).setPosition(0);
        rightSplitPane.getDividers().get(0).setPosition(0);
        splitPane2.autosize();
    }


    public void update(){
        Platform.runLater(()-> {
            Region region = Main.game.getRegion();
            TreeItem<String> rootTree = treeView.getRoot();
            for (Player aPlayer : region.getPlayers()) {
                TreeViewUtils.updatePlayerBranche(rootTree,aPlayer);
            }
            TreeViewUtils.checkAndDelExedent(rootTree,region.getPlayers());
            weatherToday.setText(region.getWeatherToday());
            weatherTomorrow.setText(region.getWeatherTomorow());
            int dayCompt = region.getTimestamp() / 24;
            int timeCompt = region.getTimestamp() % 24;
            day.setText("" + dayCompt);
            time.setText("" + timeCompt + ":00");
        });

    }



    public void updateCanvas(){
        Platform.runLater(()->{
            Coordinate regionSize = Main.game.getRegion().getSpan();
            GraphicsContext gc = map.getGraphicsContext2D();
            Coordinate canvasSize = new Coordinate( new Float(map.getWidth()),new Float(map.getHeight()));
            gc.clearRect(0, 0, map.getWidth(), map.getHeight());
            ArrayList<Player> players = Main.game.getRegion().getPlayers();


            gc.setStroke(Color.BLACK);
            for(Player aPlayer : players) {
                if(aPlayer.getID().equals(selectedPlayer))
                    gc.setFill(Color.rgb(255,255,0,0.8));
                else
                    gc.setFill(Color.rgb(0,191,255,0.7));
                ArrayList<Item> items = aPlayer.getItems();
                for(Item unItem : items)
                {
                    Coordinate pos = ConvertPoss.doYouJob(regionSize, canvasSize, unItem.getLocation());
                    Coordinate range = ConvertPoss.doYouJob(regionSize,canvasSize,new Coordinate(unItem.getInfluence(),unItem.getInfluence()));
//                                              System.out.println("New Pos: " + pos.toString());
                    gc.fillOval(pos.getLatitude()-range.getLatitude(), pos.getLongitude()-range.getLongitude(), range.getLatitude()*2, range.getLongitude()*2);
                    gc.strokeOval(pos.getLatitude()-range.getLatitude(), pos.getLongitude()-range.getLongitude(), range.getLatitude()*2, range.getLongitude()*2);
                }

            }


            for(Player aPlayer : players) {

                ArrayList<Item> items = aPlayer.getItems();
                for(Item unItem : items)
                {
                    Coordinate pos = ConvertPoss.doYouJob(regionSize, canvasSize, unItem.getLocation());
                    if(unItem.getKind() == Item.KIND.AD)
                        gc.setFill(Color.TEAL);
                    else
                        gc.setFill(Color.BLUE);
//                                              System.out.println("New Pos: " + pos.toString());
                    gc.fillOval(pos.getLatitude()-(circleSize/2), pos.getLongitude()-(circleSize/2), circleSize, circleSize);

                }

            }

            gc.setFill(Color.RED);
            ArrayList<Bot> bots = Main.game.getRegion().getBots();
            for(Bot aBot : bots){
                Coordinate pos = ConvertPoss.doYouJob(regionSize, canvasSize, aBot.getLocation());
//                System.out.println("New Pos: " + pos.toString());
                gc.fillOval(pos.getLatitude()-(circleSize/2), pos.getLongitude()-(circleSize/2), circleSize, circleSize);
            }
        });




    }

    /**
     * Whait for screen size stabilized
     */
    private class StabWait extends Thread
    {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                root.widthProperty().addListener(((observable, oldValue, newValue) -> {
                    updateScreenSize();
                }));
                root.heightProperty().addListener(((observable, oldValue, newValue) -> {
                    updateScreenSize();
                }));
                UpdateScreen updateScreen = new UpdateScreen();
                updateScreen.start();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ifinit loop for update screen, get all information form serv and launch simalation
     */
    private class UpdateScreen extends Thread{
        @Override
        public void run() {
            Main.game.updateRegion();
            while(true)
            {
                while(!Main.game.updateTime()){
                    System.err.println("Metrologie get fail !");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(Main.game.isSimilationTime()){
                    Main.game.formatAndSendSales(Main.game.getSimulationModule().simulate(Main.game.getRegion()));
                }
                if(Main.game.isNewHour()){

                    while(!Main.game.updateRegion()){
                        System.err.println("Map get fail !");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else
                {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                updateCanvas();
                update();

            }
        }
    }
}
