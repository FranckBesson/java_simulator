package Broken.JavaSimulator.GameUtils;

import java.util.ArrayList;

/**
 * Created by sebastien on 21/06/17.
 */
public class Player {
    private String ID;
    private float cash;
    private int sales;
    private float profit;
    private ArrayList<Drink> Drinks = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Drink> drinksOffered = new ArrayList<>();

    public Player(String ID, float cash, int sales, float profit, ArrayList<Drink> drinks, ArrayList<Item> items, ArrayList<Drink> drinksOffered) {
        this.ID = ID;
        this.cash = cash;
        this.sales = sales;
        this.profit = profit;
        Drinks = drinks;
        this.items = items;
        this.drinksOffered = drinksOffered;
    }

    //**********************Getter******************************
    public String getID() {
        return ID;
    }

    public float getCash() {
        return cash;
    }

    public int getSales() {
        return sales;
    }

    public float getProfit() {
        return profit;
    }

    public ArrayList<Drink> getDrinks() {
        return Drinks;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
