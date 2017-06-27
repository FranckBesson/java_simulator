package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.Item;
import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.Utils.Exception.NoAdFound;
import Broken.JavaSimulator.Utils.Exception.NoStandException;
import Broken.JavaSimulator.Utils.Exception.PlayerNotFound;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.zip.Inflater;

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
            System.out.println("Ok player");
            getBranch(playerBranch,"Cash").setValue(String.valueOf(("Cash: " + playerInfo.getCash())));
            System.out.println("Ok cash");
            getBranch(playerBranch,"Sales").setValue(String.valueOf(("Sales: " + playerInfo.getSales())));
            System.out.println("Ok sales");
            getBranch(playerBranch,"Profit").setValue(String.valueOf(("Profit: " + playerInfo.getProfit())));
            System.out.println("Ok profit");
            TreeItem<String> standsBranch = getBranch(playerBranch, "Stand");
            System.out.println("Ok Stands");
            try {
                Item stands = playerInfo.getStand();
                if(standsBranch.getChildren().size() == 0){
                    TreeItem<String> poss = new TreeItem<>("Position: " + stands.getLocation().toString());
                    TreeItem<String> inf = new TreeItem<>("Influence: " + stands.getInfluence());
                    standsBranch.getChildren().addAll(poss,inf);
                }
                else
                {
                    try {
                        getBranch(standsBranch,"Positon").setValue("Position: " + stands.getLocation().toString());
                        getBranch(standsBranch,"Positon").setValue("Influence: " + stands.getInfluence());
                    }catch (PlayerNotFound e){}

                }
            } catch (NoStandException e) {
                standsBranch.getChildren().clear();
            }

            TreeItem<String> adsBranch = getBranch(playerBranch, "Ads");
            System.out.println("Ok ads");
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
                        try {
                            getBranch(unAds,"Positon").setValue("Position: " + ads.get(i).getLocation().toString());
                            getBranch(standsBranch,"Influence>").setValue("Influence: " + ads.get(i).getInfluence());
                        }catch (PlayerNotFound e){}

                        i++;
                    }
                }

            } catch (NoAdFound noAdFound) {
                adsBranch.getChildren().clear();
            }

        } catch (PlayerNotFound playerNotFound) {
            System.out.println("Create new");
            TreeItem<String> playerBranch;
            try {
                playerBranch= getBranch(root,playerInfo.getID());
            } catch (PlayerNotFound playerNotFound1) {
                playerBranch = new TreeItem<>(playerInfo.getID());
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
                playerBranch.getChildren().addAll(cash,sales,profit,stands,ads);
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



}
