package com.magicpounds.paintinventory;

public class Product {

    String category, cost, name, quantity;

    public Product()
    {}

    public Product(String category, String cost, String name, String quantity) {
        this.category = category;
        this.cost = cost;
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCost() {
        return cost;
    }

    public String getQuantity() {
        return quantity;
    }
}
