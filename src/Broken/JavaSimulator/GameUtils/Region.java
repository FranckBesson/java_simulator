package Broken.JavaSimulator.GameUtils;

import java.util.ArrayList;

/**
 * Created by sebastien on 21/06/17.
 */
public class Region {
    private String weather;
    private int timestamp;


    private ArrayList<Bot> bots = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();


    private Coordinate center;
    private Coordinate span;

    public Region(String weather, int timestamp, ArrayList<Bot> bots, ArrayList<Player> players, Coordinate center, Coordinate span) {
        this.weather = weather;
        this.timestamp = timestamp;
        this.bots = bots;
        this.players = players;
        this.center = center;
        this.span = span;
    }

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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getTimestamp() {
        return timestamp;
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
}
