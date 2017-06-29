package Broken.JavaSimulator.GameUtils;

import Broken.JavaSimulator.Utils.Exception.NoAdFound;
import Broken.JavaSimulator.Utils.Exception.NoDrinkFound;
import Broken.JavaSimulator.Utils.Exception.NoStandException;

import java.util.ArrayList;

/**
 * Created by sebastien on 21/06/17.
 */
public class Player {
    private String ID;
    private float cash;
    private int sales;
    private float profit;
    private ArrayList<Drink> drinks = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Drink> drinksOffered = new ArrayList<>();

    public Player(String ID, float cash, int sales, float profit, ArrayList<Drink> drinks, ArrayList<Item> items, ArrayList<Drink> drinksOffered) {
        this.ID = ID;
        this.cash = cash;
        this.sales = sales;
        this.profit = profit;
        this.drinks = drinks;
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

    public ArrayList<Drink> getDrinks() throws NoDrinkFound {
        if(drinks.size()!= 0)
            return drinks;
        throw new NoDrinkFound();
    }

    public ArrayList<Drink> getDrinksOffered() throws NoDrinkFound {
        //System.out.println("[Player]Drink inf "+getID()+" -> "+drinksOffered.size());
        if(drinksOffered.size()!= 0)
            return drinksOffered;
        throw new NoDrinkFound();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item getStand() throws NoStandException {
        for(Item unItem : items){
            if(unItem.getKind() == Item.KIND.STAND)
                return unItem;
        }
        throw new NoStandException();
    }

    public ArrayList<Item> getAds() throws NoAdFound {
        ArrayList<Item> temp = new ArrayList<>();
        for(Item unItem : items){
            if(unItem.getKind() == Item.KIND.AD)
                temp.add(unItem);
        }
        if(temp.size() != 0){
            return temp;
        }
        throw new NoAdFound();
    }
}
