package Broken.JavaSimulator.GameUtils;

import Broken.JavaSimulator.Utils.Exception.PlayerNotFound;

import java.util.ArrayList;

/**
 * Created by sebastien on 21/06/17.
 */
public class Region {
    private String weatherToday;
    private String weatherTomorow;
    private int timestamp;

    private ArrayList<Bot> bots = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();

    private Coordinate center;
    private Coordinate span;

    public Region(String weatherToday, int timestamp, ArrayList<Bot> bots, ArrayList<Player> players, Coordinate center, Coordinate span) {
        this.weatherToday = weatherToday;
        this.timestamp = timestamp;
        this.bots = bots;
        this.players = players;
        this.center = center;
        this.span = span;
    }
    public Region(){}

    public Region(ArrayList<Player> players, Coordinate center, Coordinate span) {
        this.players = players;
        this.center = center;
        this.span = span;
    }

    public void updateWhithR3(ArrayList<Player> players, Coordinate center, Coordinate span){
        this.players = players;
        this.center = center;
        this.span = span;
    }

    //**********************Getter and Setter******************************

    public String getWeatherToday() {
        return weatherToday;
    }

    public void setWeatherToday(String weatherToday) {
        this.weatherToday = weatherToday;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Player getPlayerById(String id) throws PlayerNotFound {
        for(Player aPlayer : players){
            if(aPlayer.getID().equals(id)){
                return aPlayer;
            }
        }
        throw new PlayerNotFound();
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Bot> getBots() {
        return bots;
    }

    public void setBots(ArrayList<Bot> bots) {
        this.bots = bots;
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public Coordinate getSpan() {
        return span;
    }

    public void setSpan(Coordinate span) {
        this.span = span;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getWeatherTomorow() {
        return weatherTomorow;
    }

    public void setWeatherTomorow(String weatherTomorow) {
        this.weatherTomorow = weatherTomorow;
    }
}
