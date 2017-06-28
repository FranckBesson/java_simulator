package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.Drink;
import Broken.JavaSimulator.GameUtils.Item;
import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.Utils.Exception.NoAdFound;
import Broken.JavaSimulator.Utils.Exception.NoDrinkFound;
import Broken.JavaSimulator.Utils.Exception.NoStandException;
import Broken.JavaSimulator.Utils.Exception.PlayerNotFound;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Created by sebastien on 27/06/17.
 */
public class TreeViewUtils {
    public static TreeItem<String> getPlayerBranch(TreeItem<String> root, String playerID) throws PlayerNotFound {
        ObservableList<TreeItem<String>> playersBranch = root.getChildren();
        for(TreeItem<String> aBranche : playersBranch){
            if(aBranche.getValue().equals(playerID)){
                return aBranche;
            }
        }
        throw new PlayerNotFound();
    }

    private static TreeItem<String> getBranch(TreeItem<String> root, String ID) throws PlayerNotFound {
        ObservableList<TreeItem<String>> playersBranch = root.getChildren();
        for(TreeItem<String> aBranche : playersBranch){
            if(aBranche.getValue().contains(ID)){
                return aBranche;
            }
        }
        throw new PlayerNotFound();
    }

    public static void updatePlayerBranche(TreeItem<String> root, Player playerInfo){

        try {

            TreeItem<String> playerBranch = getPlayerBranch(root,playerInfo.getID());
//            System.out.println("Ok player");
            getBranch(playerBranch,"Cash").setValue(String.valueOf(("Cash: " + playerInfo.getCash())));
//            System.out.println("Ok cash");
            getBranch(playerBranch,"Sales").setValue(String.valueOf(("Sales: " + playerInfo.getSales())));
//            System.out.println("Ok sales");
            getBranch(playerBranch,"Profit").setValue(String.valueOf(("Profit: " + playerInfo.getProfit())));
//            System.out.println("Ok profit");
            TreeItem<String> standsBranch = getBranch(playerBranch, "Stand");
//            System.out.println("Ok Stands");
            try {
                Item stands = playerInfo.getStand();
                if(standsBranch.getChildren().size() == 0){
                    TreeItem<String> poss = new TreeItem<>("Position: " + stands.getLocation().toString());
                    TreeItem<String> inf = new TreeItem<>("Influence: " + stands.getInfluence());
                    standsBranch.getChildren().addAll(poss,inf);
                }
                else
                {

                    getBranch(standsBranch,"Positon").setValue("Position: " + stands.getLocation().toString());
                    getBranch(standsBranch,"Positon").setValue("Influence: " + stands.getInfluence());


                }
            } catch (NoStandException e) {
                standsBranch.getChildren().clear();
            }

            TreeItem<String> adsBranch = getBranch(playerBranch, "Ads");
//            System.out.println("Ok ads");
            try {
                ArrayList<Item> ads = playerInfo.getAds();
                if(ads.size()!=adsBranch.getChildren().size()){
                    adsBranch.getChildren().clear();
                    for(Item unAd : ads){
                        addAd(adsBranch,unAd);
                    }
                }
                else
                {
                    int i = 0;
                    for(TreeItem<String> unAds : adsBranch.getChildren()){

                        getBranch(unAds,"Positon").setValue("Position: " + ads.get(i).getLocation().toString());
                        getBranch(standsBranch,"Influence>").setValue("Influence: " + ads.get(i).getInfluence());

                        i++;
                    }
                }

            } catch (NoAdFound noAdFound) {
                adsBranch.getChildren().clear();
            }
            TreeItem<String> drinksBranch = getBranch(playerBranch, "Drinks");
            try {
                checkDrinks(playerInfo,drinksBranch);

            } catch (NoDrinkFound noDrinkFound) {
                noDrinkFound.printStackTrace();
            }


        } catch (PlayerNotFound playerNotFound) {
//            System.out.println("Create new");
            TreeItem<String> playerBranch;
            try {
                playerBranch= getBranch(root,playerInfo.getID());
            } catch (PlayerNotFound playerNotFound1) {
                ImageView playerIcon = new ImageView(new Image("Broken/JavaSimulator/resources/playerIcon.png"));
                playerIcon.setPreserveRatio(true);
                playerIcon.setSmooth(true);
                playerBranch = new TreeItem<>(playerInfo.getID(),playerIcon);
                root.getChildren().add(playerBranch);
                TreeItem<String> cash = new TreeItem<>(("Cash: " + playerInfo.getCash()));
                TreeItem<String> sales = new TreeItem<>(("Sales: " + playerInfo.getSales()));
                TreeItem<String> profit = new TreeItem<>(("Profit: " + playerInfo.getProfit()));
                TreeItem<String> stands = new TreeItem<>("Stand");

                try {
                    Item standsInfo = playerInfo.getStand();
                    TreeItem<String> poss = new TreeItem<>("Position: " + standsInfo.getLocation().toString());
                    TreeItem<String> inf = new TreeItem<>("Influence: " + standsInfo.getInfluence());
                    stands.getChildren().addAll(poss,inf);
                } catch (NoStandException e) {}

                TreeItem<String> ads = new TreeItem<>("Ads");
                try {
                    ArrayList<Item> adsInfo = playerInfo.getAds();
                    for(Item unAd : adsInfo){
                        addAd(ads,unAd);
                    }
                } catch (NoAdFound noAdFound) {
                    ads.getChildren().clear();
                }
                TreeItem<String> drinks = new TreeItem<>("Drinks");
                try {
                    ArrayList<Drink> drinksArray = playerInfo.getDrinks();
                    for(Drink aDrink : drinksArray){
                        addDrink(drinks,aDrink);
                    }
                } catch (NoDrinkFound noDrinkFound) {
                    noDrinkFound.printStackTrace();
                }
                TreeItem<String> drinksOfferd = new TreeItem<>("Drinks Offered");
                try {
                    ArrayList<Drink> drinksArray = playerInfo.getDrinksOffered();
                    for(Drink aDrink : drinksArray){
                        addDrink(drinksOfferd,aDrink);
                    }
                } catch (NoDrinkFound noDrinkFound) {
                    noDrinkFound.printStackTrace();
                }




                playerBranch.getChildren().addAll(cash,sales,profit,stands,ads,drinks,drinksOfferd);

            }

        }

    }

    private static void addAd(TreeItem<String> adsBranch, Item ad){
        int size = adsBranch.getChildren().size();
        TreeItem<String> poss = new TreeItem<>("Position: " + ad.getLocation().toString());
        TreeItem<String> inf = new TreeItem<>("Influence: " + ad.getInfluence());
        TreeItem<String> ads = new TreeItem<>(String.valueOf(size+1));
        ads.getChildren().addAll(poss,inf);
        adsBranch.getChildren().add(ads);
    }



    private static void addDrink(TreeItem<String> drinksBranch, Drink drink){
        TreeItem<String> price = new TreeItem<>(("Price: "+String.valueOf(drink.getPrice())));
        TreeItem<String> hasAlcool = new TreeItem<>(("Has Alcool: "+String.valueOf(drink.hasAlcohol())));
        TreeItem<String> isCold = new TreeItem<>(("Is Cool: "+String.valueOf(drink.isCold())));
        TreeItem<String> name = new TreeItem<>(drink.getName());
        name.getChildren().addAll(price,hasAlcool,isCold);
        drinksBranch.getChildren().add(name);
    }

    private static void checkDrinks(Player playerInfo, TreeItem<String> drinksBranch) throws NoDrinkFound, PlayerNotFound {
        ArrayList<Drink> drinks = playerInfo.getDrinks();
        if(drinks.size()!=drinksBranch.getChildren().size()){
            drinksBranch.getChildren().clear();
            for(Drink aDrink : drinks){
                addDrink(drinksBranch,aDrink);
            }
        }
        else
        {
            for(Drink aDrink : drinks){
                TreeItem<String> thisDrink = getBranch(drinksBranch, aDrink.getName());

                getBranch(thisDrink,"Price").setValue(("Price: "+String.valueOf(aDrink.getPrice())));
                getBranch(thisDrink,"Has Alcool").setValue("Has Alcool: "+String.valueOf(aDrink.hasAlcohol()));
                getBranch(thisDrink,"Is Cool").setValue(("Is Cool: "+String.valueOf(aDrink.isCold())));

            }
        }
    }

    public static void checkAndDelExedent(TreeItem<String> root, ArrayList<Player> playerInfo){
        ObservableList<TreeItem<String>> playersBranch = root.getChildren();
        for(TreeItem<String> aBranch : playersBranch){
            boolean find = false;
            for(Player aPlayer : playerInfo){
//                System.out.println(aBranch.getValue()+" == "+aPlayer.getID()+" => "+aPlayer.getID().contains(aBranch.getValue()));
                if(aPlayer.getID().contains(aBranch.getValue())){
                    find = true;
                    break;
                }

            }
            if (!find){
                System.out.println("Delleting: "+aBranch.getValue());
                playersBranch.remove(aBranch);
            }
        }
    }



}
