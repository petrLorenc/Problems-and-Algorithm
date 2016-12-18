package cz.lorenc.model;

/**
 * Created by petr.lorenc on 09.12.16.
 */
public class Item {
    int weight;
    int price;

    public Item(int weight, int price) {
        this.weight = weight;
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "weight=" + weight +
                ", price=" + price +
                '}';
    }
}