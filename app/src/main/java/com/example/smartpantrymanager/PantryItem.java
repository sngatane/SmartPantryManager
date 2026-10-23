
package com.example.smartpantrymanager;

public class PantryItem {
    public int id;
    public String name;
    public double quantity;
    public String unit;
    public String expiryDate;

    public PantryItem(int id, String name, double quantity,
                      String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return name + " — " + quantity + " " + unit;
    }
}
