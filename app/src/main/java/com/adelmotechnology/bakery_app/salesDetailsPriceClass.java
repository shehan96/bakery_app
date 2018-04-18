package com.adelmotechnology.bakery_app;

/**
 * Created by IMPERIUM on 4/17/2018.
 */

public class salesDetailsPriceClass {

    String name;
    int price;
    int qty;

    public salesDetailsPriceClass() {
    }

    public salesDetailsPriceClass(String name, int price,int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
