package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 21/06/17.
 */
public class Bot {
    private Coordinate location;

    public Bot(Coordinate location) {
        this.location = location;
    }

    //**********************Getter and Setter******************************
    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }
}
