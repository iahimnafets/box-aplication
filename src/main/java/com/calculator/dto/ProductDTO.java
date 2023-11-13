package com.calculator.dto;

public class ProductDTO {
    private int id;
    private double weight;
    private int price;

    public ProductDTO(int id, double weight, int price) {
        this.id = id;
        this.weight = weight;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}