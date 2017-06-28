package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.*;
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

    public Simulation() {
        weatherProb.put("THUNDERSTORM",0.0);
        weatherProb.put("RAINY",0.15);
        weatherProb.put("CLOUDY",0.3);
        weatherProb.put("SUNNY",0.75);
        weatherProb.put("HEATWAVE",1.0);
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
        placeBot(20,region);
        ArrayList<Player> players = region.getPlayers();
        ArrayList<Bot> bots = region.getBots();
        HashMap<Player,Integer> botDispatch = new HashMap<>();
        for(Bot abot : bots) {

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
                if (botDispatch.containsKey(savedPlayers))
                    botDispatch.replace(savedPlayers, botDispatch.get(savedPlayers) + 1);
                else
                    botDispatch.put(savedPlayers, 1);


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
                       sales.put(key.getID(),new Sale(key.getID(),key.getDrinks().get(0).getName(),1));
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



}
