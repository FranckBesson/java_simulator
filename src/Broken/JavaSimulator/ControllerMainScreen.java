package Broken.JavaSimulator;

import Broken.JavaSimulator.GameUtils.Drink;
import Broken.JavaSimulator.GameUtils.Item;
import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.GameUtils.Region;
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
    private SplitPane splitPane2;
    private Canvas map;
    private SplitPane splitPane;
    private SplitPane rightSplitPane;
    private VBox timeVbox;
    private VBox dayVbox;
    private TreeView<String> treeView;

    private Boolean first = true;


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



        TreeItem<String> rootPlayer = new TreeItem<>();
        treeView = new TreeView<String>(rootPlayer);
        treeView.setShowRoot(false);

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
        System.out.println(root.getWidth());
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
        Region region = Main.game.getRegion();
        TreeItem<String> rootTree = treeView.getRoot();
        rootTree.getChildren().clear();
        for (Player aPlayer : region.getPlayers()){
            TreeItem<String> playerItem = new TreeItem<>(aPlayer.getID());
            TreeItem<String> cash = new TreeItem<>("Cash: "+aPlayer.getCash());
            TreeItem<String> sales= new TreeItem<>("Sales: "+aPlayer.getSales());
            TreeItem<String> profit = new TreeItem<>("Profit: "+aPlayer.getProfit());
            TreeItem<String> stands = new TreeItem<>("Stand");
            TreeItem<String> ads = new TreeItem<>("Ads");
            int iStand = 1;
            int iAd = 1;
            for(Item aItem : aPlayer.getItems()){
                TreeItem<String> poss = new TreeItem<>("Position: "+aItem.getLocation().toString());
                TreeItem<String> inf = new TreeItem<>("Influence: "+aItem.getInfluence());


                if(aItem.getKind() == Item.KIND.AD)
                {
                    TreeItem<String> node = new TreeItem<>(""+iAd);
                    node.getChildren().addAll(poss,inf);
                    ads.getChildren().add(node);
                    iAd++;
                }

                else{
                    TreeItem<String> node = new TreeItem<>(""+iStand);
                    node.getChildren().addAll(poss,inf);
                    stands.getChildren().add(node);
                    iStand++;
                }
            }
            TreeItem<String> drinks = new TreeItem<>("Drinks");
            for(Drink aDrink : aPlayer.getDrinks()){
                TreeItem<String> price = new TreeItem<>("Price: "+aDrink.getPrice()+"€");
                TreeItem<String> hasAlcohol = new TreeItem<>("Has Alcohol: "+aDrink.HasAlcohol());
                TreeItem<String> isCold = new TreeItem<>("Is Cold: "+aDrink.isCold());
                TreeItem<String> node = new TreeItem<>(aDrink.getName());
                node.getChildren().addAll(price,hasAlcohol,isCold);
                drinks.getChildren().add(node);
            }

            playerItem.getChildren().addAll(cash,sales,profit,ads,stands);
            rootTree.getChildren().add(playerItem);

            weatherToday.setText(region.getWeatherToday());
            weatherTomorrow.setText(region.getWeatherTomorow());
            int dayCompt = region.getTimestamp()/24;
            int timeCompt = region.getTimestamp()%24;
            day.setText(""+dayCompt);
            time.setText(""+timeCompt+":00");


        }

    }

    //Thread wait for screen stabilisation
    private class StabWait extends Thread
    {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                root.widthProperty().addListener(((observable, oldValue, newValue) -> {
                    updateScreenSize();
                }));
                UpdateScreen updateScreen = new UpdateScreen();
                updateScreen.start();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class UpdateScreen extends Thread{
        @Override
        public void run() {
            while(true)
            {
                Main.game.updateTime();
                Main.game.updateRegion();
                update();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
