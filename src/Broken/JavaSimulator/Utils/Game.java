package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by sebastien on 21/06/17.
 * Contains all games utils
 *
 */
public class Game {
    private Communication communicationModule;
    private Simulation simulationModule;
    private Region region = new Region();
    private ArrayList<Player> players = new ArrayList<>();
    private Boolean jsonError = false;
    private Boolean comError = false;
    private int savedHour = 1;
    private boolean flagDay = false;



    public Game(String serveuradrr) {
        communicationModule = new Communication(serveuradrr);
        simulationModule = new Simulation();

    }

    /**
     * Send R2 request and update all
     * @return
     */
    public boolean updateRegion()
    {
        try {


            JSONObject all = communicationModule.get("/map");
            JSONObject mapJson = all.getJSONObject("map");
            JSONArray jsonPlayers = mapJson.getJSONArray("ranking");
            JSONObject jsonPlayerInfo = mapJson.getJSONObject("playerInfo");
            JSONObject jsonItems = mapJson.getJSONObject("itemsByPlayer");
            JSONObject jsonDrink = mapJson.getJSONObject("drinksByPlayer");
            JSONObject regionJson = mapJson.getJSONObject("region");
            players.clear();
            for(int i = 0 ; i<jsonPlayers.length(); i++) {
                //get the curent player ID/name
                String curentID =jsonPlayers.getString(i);
//                System.out.println("Player id: "+curentID);
                float curentCash = 0;
                float curentProfit = 0;
                int curentSales = 0;
                JSONObject curentPlayerInfo = null;

                try{
                    //Get object contain all player infos
                    curentPlayerInfo = jsonPlayerInfo.getJSONObject(curentID);
                    curentCash = curentPlayerInfo.getBigDecimal("cash").floatValue();
                    //System.out.println("Cash: " +curentCash);
                    curentProfit = curentPlayerInfo.getBigDecimal("profit").floatValue();
                   // System.out.println("Profit: " + curentProfit);
                    curentSales = curentPlayerInfo.getInt("sales");
                    //System.out.println("Profit: " + curentSales);
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                //colect all items of this player
                ArrayList<Item> curentItems = new ArrayList<>();
                try {
                    JSONArray curentJsonItems = jsonItems.getJSONArray(curentID);
                    curentItems = this.getItems(curentJsonItems);
                }catch (JSONException e){
                    System.err.println(e.getMessage());
                }
                //colect all drinks in drinks offered of this player
                ArrayList<Drink> drinksOffered = new ArrayList<>();
                if(curentPlayerInfo != null)
                    drinksOffered = this.getDrinks(curentPlayerInfo.getJSONArray("drinksOffered"));
                if(drinksOffered.size() == 0){
                    //System.out.println("0 Drinks offered for "+curentID);
                }
                for(Drink aDrink : drinksOffered){
                    System.out.println("[game][update]Drinks offered "+curentID+": "+aDrink.getName());
                }
                //colect all drinks in drinks of this player
                ArrayList<Drink> curentDrinks = new ArrayList<>();
                try{
                    JSONArray curentJsonPlayerDrinks = jsonDrink.getJSONArray(curentID);
                    curentDrinks = this.getDrinks(curentJsonPlayerDrinks);
                }catch (JSONException e)
                {
                    System.err.println(e.getMessage());
                }


                players.add(new Player(curentID,curentCash,curentSales,curentProfit,curentDrinks,curentItems,drinksOffered));


            }

            //Collect region infos
            Coordinate regionCenter = new Coordinate(regionJson.getJSONObject("center").getBigDecimal("latitude").floatValue(),regionJson.getJSONObject("center").getBigDecimal("longitude").floatValue());
            Coordinate regionSpan = new Coordinate(regionJson.getJSONObject("span").getBigDecimal("latitudeSpan").floatValue(),regionJson.getJSONObject("span").getBigDecimal("longitudeSpan").floatValue());
            if(region == null)
                region = new Region(players,regionCenter,regionSpan);
            else
                region.updateWhithR3(players,regionCenter,regionSpan);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if(!comError){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Communication error!");
                    alert.show();
                });
                comError = true;

            }
            return false;


        }catch (JSONException e) {
            e.printStackTrace();
            if(!jsonError)
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error while decripting JSON response");
                    alert.setHeaderText("Communication Error !");
                    alert.show();
                });
            jsonError = true;
            return false;

        }

    }

    /**
     * Send R7 and update all (metrology)
     * @return if resquest was works
     */
    public boolean updateTime(){
        try {
            JSONObject r7 = communicationModule.get("/metrology");
            region.setTimestamp(r7.getBigDecimal("timestamp").intValue());
            JSONArray weather = r7.getJSONArray("weather");
            for(int i = 0; i<weather.length();i++)
            {
                JSONObject aWeather = weather.getJSONObject(i);
                if(aWeather.getBigDecimal("dfn").intValue() == 0){
                    region.setWeatherToday(aWeather.getString("weather"));
                }
                else
                {
                    region.setWeatherTomorow(aWeather.getString("weather"));
                }
            }
            return true;

            
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * return all items contains in a JSONArray
     * @param arrayItems
     * @return ArrayList of Item
     */
    private ArrayList<Item> getItems(JSONArray arrayItems)
    {
        ArrayList<Item> tempImtems = new ArrayList<>();
        for (int j = 0;  j<arrayItems.length(); j++)
        {
            JSONObject curentIT = arrayItems.getJSONObject(j);
            Item.KIND curentKind;
            if(curentIT.get("kind").equals("AD"))
                curentKind = Item.KIND.AD;
            else
                curentKind = Item.KIND.STAND;
            tempImtems.add(new Item(curentKind,new Coordinate(curentIT.getJSONObject("location").getBigDecimal("latitude").floatValue(),curentIT.getJSONObject("location").getBigDecimal("longitude").floatValue()),curentIT.getBigDecimal("influence").floatValue()));

        }
        return tempImtems;
    }

    /**
     * return all drink contains in a JSON Array
     * @param arrayDrinks
     * @return
     */
    private ArrayList<Drink> getDrinks(JSONArray arrayDrinks)
    {
        ArrayList<Drink> tempDrinks = new ArrayList<>();
        for (int j = 0; j<arrayDrinks.length(); j++)
        {
            JSONObject curentJsonDrink = arrayDrinks.getJSONObject(0);
            Drink curentDrink = new Drink(curentJsonDrink.getString("name"),curentJsonDrink.getBigDecimal("price").floatValue(),curentJsonDrink.getBoolean("hasAlcohol"),curentJsonDrink.getBoolean("isCold"));
            //System.out.println("\t"+curentDrink.toString());
            tempDrinks.add(curentDrink);
        }
        return tempDrinks;
    }
    /**
     * Format sales into JSON and send it to server
     * @param sales
     */
    public void formatAndSendSales(ArrayList<Sale> sales){
        JSONArray salesArray  = new JSONArray();
        for(Sale aSale : sales){
            JSONObject curentSale = new JSONObject();
            curentSale.put("player",aSale.getPlayer());
            curentSale.put("item",aSale.getItem());
            curentSale.put("quantity",aSale.getQuantity());
            salesArray.put(curentSale);
        }
        JSONObject salesMain = new JSONObject();
        salesMain.put("sales",salesArray);
        System.out.println("[sendGame]"+salesMain.toString());

        try {
            communicationModule.post("/sales",salesMain);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Check if is a nex Hour
     * @return
     */
    public Boolean isNewHour(){
        int hour = region.getTimestamp() % 24;
        if(hour != savedHour){
            savedHour = hour;
            return true;
        }
        return false;
    }

    /**
     * Check if is 23h (for launch simulation)
     * @return
     */
    public Boolean isSimilationTime(){
        int heure = region.getTimestamp() % 24;
        if(heure == 22 && !flagDay){
            flagDay = true;
            return true;
        }
        else if(heure != 22){
            flagDay = false;
            return false;
        }
        return false;
    }


    /*---------------------------------------------------------------------------
        Geters
     ----------------------------------------------------------------------------*/

    public Region getRegion() {
        return region;
    }

    public Simulation getSimulationModule() {
        return simulationModule;
    }
}
