package com.coffeeshop.model;

public class Product extends BaseEntity {
    private int categoryId;
    private String name;
    private double price;
    private int stock;
    private boolean active;

    public Product() {
    }

    public Product(int categoryId, String name, double price, int stock, boolean active) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.active = active;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
