package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.Item;
import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.Utils.Exception.NoStandException;
import Broken.JavaSimulator.Utils.Exception.PlayerNotFound;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;

/**
 * Created by sebastien on 27/06/17.
 */
public class TreeViewUtils {
    public static TreeItem<String> getPlayerBranch(TreeItem<String> root, String playerID) throws PlayerNotFound {
        ObservableList<TreeItem<String>> playersBranch = root.getChildren();
        for(TreeItem<String> aBranche : playersBranch){
            if(aBranche.getValue().matches("(.*)"+playerID+"(.*)")){
                return aBranche;
            }
        }
        throw new PlayerNotFound();
    }

    public static void updatePlayerBranche(TreeItem<String> playerBranch, Player playerInfo){
        try {
            getPlayerBranch(playerBranch,"Cash").setValue(String.valueOf(playerInfo.getCash()));
            getPlayerBranch(playerBranch,"Sales").setValue(String.valueOf(playerInfo.getSales()));
            getPlayerBranch(playerBranch,"Profit").setValue(String.valueOf(playerInfo.getProfit()));
            ObservableList<TreeItem<String>> childrens = getPlayerBranch(playerBranch, "Stands").getChildren();
            try {
                if(childrens.size() == 0){

                    Item stands = playerInfo.getStand();
                    TreeItem<String> poss = new TreeItem<>("Position: " + stands.getLocation().toString());
                    TreeItem<String> inf = new TreeItem<>("Influence: " + stands.getInfluence());
                    //TODO
                }
            } catch (NoStandException e) {
                e.printStackTrace();
            }

        } catch (PlayerNotFound playerNotFound) {
            playerNotFound.printStackTrace();
        }
    }



}
