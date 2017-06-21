package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.Coordinate;
import Broken.JavaSimulator.GameUtils.Item;
import Broken.JavaSimulator.GameUtils.Player;
import Broken.JavaSimulator.GameUtils.Region;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastien on 21/06/17.
 */
public class Game {
    Communication communicationModule;
    Region region;
    ArrayList<Player> players = new ArrayList<>();

    public Game(String serveuradrr) {
        communicationModule = new Communication(serveuradrr);
    }

    public void updateRegion()
    {
        try {
            JSONObject mapJson = communicationModule.get("/");
            JSONArray jsonPlayers = mapJson.getJSONArray("ranking");
            JSONObject jsonPlayerInfo = mapJson.getJSONObject("playerInfo");
            JSONObject jsonItems = mapJson.getJSONObject("itemsByPlayer");
            players.clear();
            for(int i = 0 ; i<jsonPlayers.length(); i++) {
                String curentID =jsonPlayers.getString(i);
                System.out.println(curentID);

                JSONObject curentPlayerInfo = jsonPlayerInfo.getJSONObject(curentID);
                float curentCash = curentPlayerInfo.getBigDecimal("cash").floatValue();
                System.out.println(curentCash);

                float curentProfit = curentPlayerInfo.getBigDecimal("profit").floatValue();
                System.out.println(curentProfit);

                int curentSales = curentPlayerInfo.getInt("sales");
                System.out.println(curentSales);

                JSONArray curentJsonItems = jsonItems.getJSONArray(curentID);
                System.out.println(curentJsonItems);
                ArrayList<Item> curentItems = new ArrayList<>();
                for (int j = 0;  j<curentJsonItems.length(); j++)
                {
                    JSONObject curentIT = curentJsonItems.getJSONObject(j);
                    Item.KIND curentKind;
                    if(curentIT.get("kind").equals("ad"))
                        curentKind = Item.KIND.AD;
                    else
                        curentKind = Item.KIND.STAND;
                    curentItems.add(new Item(curentKind,new Coordinate(curentIT.getJSONObject("location").getBigDecimal("latitude").floatValue(),curentIT.getJSONObject("location").getBigDecimal("longitude").floatValue()),new Coordinate(curentIT.getJSONObject("location").getBigDecimal("latitude").floatValue(),curentIT.getJSONObject("location").getBigDecimal("longitude").floatValue())));
                    System.out.println(curentItems.get(j).toString());
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
