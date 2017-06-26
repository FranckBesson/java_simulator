package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.*;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastien on 26/06/17.
 */
public class Simulation {
    HashMap<String,Double> weatherProb = new HashMap<>();

    public Simulation() {
        weatherProb.put("thunderstorm",0.0);
        weatherProb.put("rainy",0.15);
        weatherProb.put("cloudy",0.3);
        weatherProb.put("sunny",0.75);
        weatherProb.put("heatwave",1.0);
    }

    public void placeBot(int nbrOfBot, Region region){

        region.getBots().clear();
        for(int i = 0; i<nbrOfBot; i++)
        {
            BigDecimal lattitude = BigDecimal.valueOf(Math.random()*(region.getSpan().getLatitude()*2));
            BigDecimal longitude = BigDecimal.valueOf(Math.random()*(region.getSpan().getLongitude()*2));
            region.getBots().add(new Bot(new Coordinate(lattitude.floatValue(),longitude.floatValue())));
        }

        for(Bot aBot : region.getBots()){
            System.out.println(aBot.getLocation().toString());
        }
        System.out.println("distance : "+checkDistance(region.getPlayers().get(0).getItems().get(0),region.getBots().get(0)));

    }

    public float checkDistance(Item stand, Bot abot)
    {
        float distance = (float) Math.sqrt(Math.pow(stand.getLocation().getLatitude()-abot.getLocation().getLatitude(),2)+Math.pow(stand.getLocation().getLongitude()-abot.getLocation().getLongitude(),2));
        return distance;
    }


    public void simulate(Region region){
        placeBot(20,region);
        ArrayList<Player> players = region.getPlayers();
        ArrayList<Bot> bots = region.getBots();
        HashMap<Player,Integer> botDispatch = new HashMap<>();
        for(Bot abot : bots) {
            float oldDistance = Float.MAX_VALUE;
            Player savedPlayers = null;
            for (Player aPlayer : players) {
                float distance = checkDistance(aPlayer.getItems().get(0), abot);
                if (distance < oldDistance) {
                    savedPlayers = aPlayer;
                    oldDistance = distance;
                }
            }
            if (botDispatch.containsKey(savedPlayers))
                botDispatch.replace(savedPlayers, botDispatch.get(savedPlayers) + 1);
            else
                botDispatch.put(savedPlayers, 1);
        }

        Double todayProb = weatherProb.get(region.getWeatherToday());

        for(Map.Entry<Player, Integer> entry : botDispatch.entrySet()) {
            Player key = entry.getKey();
            Integer value = entry.getValue();

            for(int i = 0; i<value; i++){
               //TODO Finish proba
            }

            // do what you have to do here
            // In your case, an other loop.
        }

    }



}
