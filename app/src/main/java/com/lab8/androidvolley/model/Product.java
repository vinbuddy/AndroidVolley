package com.lab8.androidvolley.model;

import java.util.Random;

public class Product {
    public int id;
    public String name;

    public double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static Product generateFakeProduct() {
        Random random = new Random();
        Product product = new Product();
        int id = random.nextInt(1000); // Generate random number
        String name = "Product " + id; // Generate name based on random number
        double price = random.nextDouble() * 100; // Generate random price

        product.setName(name);
        product.setPrice(price);

        return product;
    }



}
