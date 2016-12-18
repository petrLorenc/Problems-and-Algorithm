package cz.lorenc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr.lorenc on 11.12.16.
 */
public class LoadProblem {
    int ID;
    int numberOfItem;
    int capacity;
    List<Item> items;

    public LoadProblem(int ID, int numberOfItem, int capacity) {
        this.ID = ID;
        this.numberOfItem = numberOfItem;
        this.capacity = capacity;
        this.items = new ArrayList<>(this.capacity);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int getID() {
        return ID;
    }

    public int getNumberOfItem() {
        return numberOfItem;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "ID=" + ID +
                ", numberOfItem=" + numberOfItem +
                ", capacity=" + capacity +
                ", items=" + items +
                '}';
    }

}
