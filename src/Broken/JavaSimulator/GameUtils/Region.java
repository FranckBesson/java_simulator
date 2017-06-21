package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 21/06/17.
 */
public class Region {
    private String weather;
    private int timestamp;


    private Bot[] bots;
    private Player[] players;


    private Coordinate center;
    private Coordinate span;

    public Region(String weather, int timestamp, Bot[] bots, Player[] players, Coordinate center, Coordinate span) {
        this.weather = weather;
        this.timestamp = timestamp;
        this.bots = bots;
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

    public Bot[] getBots() {
        return bots;
    }

    public void setBots(Bot[] bots) {
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

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
