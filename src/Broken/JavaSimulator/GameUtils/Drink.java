package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 21/06/17.
 */
public class Drink {
    private String name;
    private float price;


    @Override
    public String toString() {
        return "Name: "+name+" Price: "+price;
    }

    //**********************Getter and Setter******************************
    public Drink(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

}
