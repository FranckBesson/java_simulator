package Broken.JavaSimulator.GameUtils;

/**
 * Created by sebastien on 22/06/17.
 */
public class Sale {
    private String player;
    private String item;
    private int quantity;

    public Sale(String player, String item, int quantity) {
        this.player = player;
        this.item = item;
        this.quantity = quantity;
    }

    public String getPlayer() {
        return player;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increment(){
        this.quantity++;
    }
}
