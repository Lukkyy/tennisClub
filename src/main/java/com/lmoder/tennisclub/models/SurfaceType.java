package com.lmoder.tennisclub.models;

public enum SurfaceType {
    GRASS(15), HARD(10), CLAY(12), ARTIFICIAL(13);

    private double price;
    SurfaceType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
