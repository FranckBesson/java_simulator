package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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


            JSONObject mapJson = communicationModule.get("/map");
            JSONArray jsonPlayers = mapJson.getJSONArray("ranking");
            JSONObject jsonPlayerInfo = mapJson.getJSONObject("playerInfo");
            JSONObject jsonItems = mapJson.getJSONObject("itemsByPlayer");
            JSONObject jsonDrink = mapJson.getJSONObject("drinksByPlayer");
            JSONObject regionJson = mapJson.getJSONObject("region");
            players.clear();
            for(int i = 0 ; i<jsonPlayers.length(); i++) {
                //get the curent player ID/name
                String curentID =jsonPlayers.getString(i);
                System.out.println("Player id: "+curentID);


                JSONObject curentPlayerInfo = jsonPlayerInfo.getJSONObject(curentID);
                float curentCash = curentPlayerInfo.getBigDecimal("cash").floatValue();
                System.out.println("Cash: " +curentCash);

                float curentProfit = curentPlayerInfo.getBigDecimal("profit").floatValue();
                System.out.println("Profit: " + curentProfit);

                int curentSales = curentPlayerInfo.getInt("sales");
                System.out.println("Profit: " + curentSales);

                JSONArray curentJsonItems = jsonItems.getJSONArray(curentID);
                ArrayList<Item> curentItems = this.getItems(curentJsonItems);

                System.out.println("Drinks Offered:");
                ArrayList<Drink> drinksOffered = this.getDrinks(curentPlayerInfo.getJSONArray("drinksOffered"));

                JSONArray curentJsonPlayerDrinks = jsonDrink.getJSONArray(curentID);
                System.out.println("Drinks:");
                ArrayList<Drink> curentDrinks = this.getDrinks(curentJsonPlayerDrinks);

                players.add(new Player(curentID,curentCash,curentSales,curentProfit,curentDrinks,curentItems,drinksOffered));
                System.out.println();


            }

            Coordinate regionCenter = new Coordinate(regionJson.getJSONObject("center").getBigDecimal("latitude").floatValue(),regionJson.getJSONObject("center").getBigDecimal("longitude").floatValue());
            Coordinate regionSpan = new Coordinate(regionJson.getJSONObject("span").getBigDecimal("latitude").floatValue(),regionJson.getJSONObject("span").getBigDecimal("longitude").floatValue());
            if(region == null)
                region = new Region(players,regionCenter,regionSpan);
            else
                region.updateWhithR3(players,regionCenter,regionSpan);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendToServer(ArrayList<Sale> sales)
    {
        JSONObject main = new JSONObject();
        JSONArray temp = new JSONArray();
        for(Sale aSlae : sales)
        {
            JSONObject curentSale = new JSONObject();
            curentSale.put("player",aSlae.getPlayer());
            curentSale.put("item",aSlae.getItem());
            curentSale.put("quatity",aSlae.getQuantity());
            temp.put(curentSale);
        }
        main.put("sales",temp);
        try {
            communicationModule.post("/sales",main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Item> getItems(JSONArray arrayItems)
    {
        ArrayList<Item> tempImtems = new ArrayList<>();
        for (int j = 0;  j<arrayItems.length(); j++)
        {
            JSONObject curentIT = arrayItems.getJSONObject(j);
            Item.KIND curentKind;
            if(curentIT.get("kind").equals("ad"))
                curentKind = Item.KIND.AD;
            else
                curentKind = Item.KIND.STAND;
            tempImtems.add(new Item(curentKind,new Coordinate(curentIT.getJSONObject("location").getBigDecimal("latitude").floatValue(),curentIT.getJSONObject("location").getBigDecimal("longitude").floatValue()),curentIT.getBigDecimal("influence").floatValue()));
            if(j == 0)
                System.out.println("Items:");
            System.out.println("\t"+tempImtems.get(j).toString());
        }
        return tempImtems;
    }




    private ArrayList<Drink> getDrinks(JSONArray arrayDrinks)
    {   //TODO Modification du protocol !!!!!!!
        ArrayList<Drink> tempDrinks = new ArrayList<>();
        for (int j = 0; j<arrayDrinks.length(); j++)
        {
            JSONObject curentJsonDrink = arrayDrinks.getJSONObject(0);
            Drink curentDrink = new Drink(curentJsonDrink.getString("name"),curentJsonDrink.getBigDecimal("price").floatValue(),curentJsonDrink.getBoolean("hasAlcohol"),curentJsonDrink.getBoolean("isCold"));
            System.out.println("\t"+curentDrink.toString());
            tempDrinks.add(curentDrink);
        }
        return tempDrinks;
    }

}
