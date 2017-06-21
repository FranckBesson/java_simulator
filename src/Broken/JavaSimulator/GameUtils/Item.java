package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 21/06/17.
 */
public class Item {
    public enum KIND{
        STAND,
        AD
    }

    private KIND kind;
    private Coordinate location;
    private Coordinate influence;

    public Item(KIND kind, Coordinate location, Coordinate influence) {
        this.kind = kind;
        this.location = location;
        this.influence = influence;
    }

    @Override
    public String toString() {
        return "Kind: "+kind+" Location: "+location.toString()+" Influence : "+ influence.toString();
    }

    //**********************Getter and Setter******************************

    public KIND getKind() {
        return kind;
    }

    public Coordinate getLocation() {
        return location;
    }

    public Coordinate getInfluence() {
        return influence;
    }
}
