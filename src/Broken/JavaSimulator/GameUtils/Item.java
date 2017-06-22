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
    private float influence;

    public Item(KIND kind, Coordinate location, float influence) {
        this.kind = kind;
        this.location = location;
        this.influence = influence;
    }

    @Override
    public String toString() {
        return "Kind: "+kind+" Location: "+location.toString()+" Influence : "+ influence;
    }

    //**********************Getter and Setter******************************

    public KIND getKind() {
        return kind;
    }

    public Coordinate getLocation() {
        return location;
    }

    public float getInfluence() {
        return influence;
    }
}
