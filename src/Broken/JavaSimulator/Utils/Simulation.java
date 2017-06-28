package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.*;
import Broken.JavaSimulator.Utils.Exception.NoAdFound;
import Broken.JavaSimulator.Utils.Exception.NoDrinkFound;
import Broken.JavaSimulator.Utils.Exception.NoStandException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastien on 26/06/17.
 */
public class Simulation {
    private HashMap<String,Double> weatherProb = new HashMap<>();
    private HashMap<String,Double> moveWeatherProb = new HashMap<>();
    private static int NBR_BOT = 50;
    public Simulation() {
        weatherProb.put("THUNDERSTORM",0.0);
        weatherProb.put("RAINY",0.15);
        weatherProb.put("CLOUDY",0.3);
        weatherProb.put("SUNNY",0.75);
        weatherProb.put("HEATWAVE",1.0);

        moveWeatherProb.put("THUNDERSTORM",0.0);
        moveWeatherProb.put("RAINY",0.10);
        moveWeatherProb.put("CLOUDY",0.4);
        moveWeatherProb.put("SUNNY",0.5);
        moveWeatherProb.put("HEATWAVE",0.2);
    }



    private void placeBot(int nbrOfBot, Region region){

        region.getBots().clear();
        for(int i = 0; i<nbrOfBot; i++)
        {
            BigDecimal lattitude = BigDecimal.valueOf(Math.random()*(region.getSpan().getLatitude()));
            BigDecimal longitude = BigDecimal.valueOf(Math.random()*(region.getSpan().getLongitude()));
            region.getBots().add(new Bot(new Coordinate(lattitude.floatValue(),longitude.floatValue())));
        }

        for(Bot aBot : region.getBots()){
            //System.out.println(aBot.getLocation().toString());
        }

    }

    private float checkDistance(Item stand, Bot abot)
    {
        float distance = (float) Math.sqrt(Math.pow(stand.getLocation().getLatitude()-abot.getLocation().getLatitude(),2)+Math.pow(stand.getLocation().getLongitude()-abot.getLocation().getLongitude(),2));
        return distance;
    }


    public ArrayList<Sale> simulate(Region region){
        placeBot(NBR_BOT,region);
        ArrayList<Player> players = (ArrayList<Player>) region.getPlayers().clone();
        ArrayList<Bot> bots = region.getBots();
        HashMap<Player,Integer> botDispatch = new HashMap<>();

        //list if the bot is in the range of one or more player
        HashMap<Bot,ArrayList<Player>> botRange = new HashMap<>();
        for(Bot aBot : bots){
            botRange.put(aBot, new ArrayList<>());
            for(Player aPlayer : players){
                try {
                    if(isOnRange(aBot,aPlayer.getStand())){
                        botRange.get(aBot).add(aPlayer);
                    }
                } catch (NoStandException e) {}

                try {
                    for(Item ad : aPlayer.getAds()){
                        if(isOnRange(aBot,ad))
                        {
                            if(!botRange.get(aBot).contains(aPlayer)){
                                botRange.get(aBot).add(aPlayer);
                            }
                        }
                    }
                } catch (NoAdFound noAdFound) {}
            }
        }

        //ingresse bot dispatch, if 1 bot is in more then 1 range, check the price
        for(Map.Entry<Bot, ArrayList<Player>> entry : botRange.entrySet()) {
            Bot key = entry.getKey();
            ArrayList<Player> value = entry.getValue();
            if(value.size()>1){
                Player savedPlayer = value.get(0);
                for(Player aPlayer : value){
                    try {
                        if(aPlayer.getDrinks().get(0).getPrice() < savedPlayer.getDrinks().get(0).getPrice()){
                            savedPlayer = aPlayer;
                        }
                    } catch (NoDrinkFound noDrinkFound) {
                        noDrinkFound.printStackTrace();
                    }
                }
                if(botDispatch.containsKey(savedPlayer)){
                    botDispatch.replace(savedPlayer,botDispatch.get(savedPlayer)+1);
                }
                else
                    botDispatch.put(savedPlayer,1);
            }
            else if(value.size() != 0){
                if(botDispatch.containsKey(value.get(0))){
                    botDispatch.replace(value.get(0),botDispatch.get(value.get(0))+1);
                }
                else
                    botDispatch.put(value.get(0),1);
            }
        }

        ArrayList<Bot> freeBot = getFreeBot(botRange);

        for(Bot abot : freeBot) {

                float oldDistance = Float.MAX_VALUE;
                Player savedPlayers = null;
                for (Player aPlayer : region.getPlayers()) {
                    try {
                        float distance = checkDistance(aPlayer.getStand(), abot);
                        if (distance < oldDistance) {
                            savedPlayers = aPlayer;
                            oldDistance = distance;
                        }
                    } catch (NoStandException e) {
                    }
                }
                if(Math.random()> weatherProb.get(region.getWeatherToday())){
                    if (botDispatch.containsKey(savedPlayers))
                        botDispatch.replace(savedPlayers, botDispatch.get(savedPlayers) + 1);
                    else
                        botDispatch.put(savedPlayers, 1);
                }



        }

        Double todayProb = weatherProb.get(region.getWeatherToday());
        HashMap<String,Sale> sales = new HashMap<>();
        for(Map.Entry<Player, Integer> entry : botDispatch.entrySet()) {
            Player key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Before Prob -> "+key.getID()+" : "+value);

            for(int i = 0; i<value; i++){
               if(Math.random()>todayProb){
                   if(!sales.containsKey(key.getID())){
                       try {
                           sales.put(key.getID(),new Sale(key.getID(),key.getDrinks().get(0).getName(),1));
                       } catch (NoDrinkFound noDrinkFound) {
                       }
                   }
                   else
                       sales.get(key.getID()).increment();

               }
            }
            if(sales.containsKey(key.getID()))
                System.out.println("After prob: "+sales.get(key.getID()).getPlayer()+" : "+sales.get(key.getID()).getQuantity());
            else
                System.out.println("After prob: "+key.getID()+" : 0");

        }
        ArrayList<Sale> salesArry = new ArrayList<>();
        salesArry.addAll(sales.values());
        return salesArry;
    }


    public boolean isOnRange(Bot bot, Item item){
        double lat = Math.pow(bot.getLocation().getLatitude()-item.getLocation().getLatitude(),2);
        double longi = Math.pow(bot.getLocation().getLongitude() - bot.getLocation().getLongitude(),2);
        return (lat + longi) < Math.pow(item.getInfluence(),2);

    }

    /**
     * return all bot who is not in range of stand or ad
     * @param botList
     * @return
     */
    private ArrayList<Bot> getFreeBot(HashMap<Bot,ArrayList<Player>> botList){
        ArrayList<Bot> temp = new ArrayList<>();
        for(Map.Entry<Bot, ArrayList<Player>> entry : botList.entrySet()) {
            Bot key = entry.getKey();
            ArrayList<Player> value = entry.getValue();
            if(value.size() == 0)
                temp.add(key);
        }
        return temp;
    }



}
