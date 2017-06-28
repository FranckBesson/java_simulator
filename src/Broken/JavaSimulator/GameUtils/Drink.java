package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 21/06/17.
 */
public class Drink {
    private String name;
    private float price;
    private boolean hasAlcohol;
    private boolean isCold;


    @Override
    public String toString() {
        return "Name: "+name+" Price: "+price;
    }

    //**********************Getter and Setter******************************


    public Drink(String name, float price, boolean hasAlcohol, boolean isCold) {
        this.name = name;
        this.price = price;
        this.hasAlcohol = hasAlcohol;
        this.isCold = isCold;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public boolean hasAlcohol() {
        return hasAlcohol;
    }

    public boolean isCold() {
        return isCold;
    }
}
