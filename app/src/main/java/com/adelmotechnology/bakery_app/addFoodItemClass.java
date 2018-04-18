package com.adelmotechnology.bakery_app;

import java.util.List;

/**
 * Created by IMPERIUM on 4/10/2018.
 */

public class addFoodItemClass {
    String name;
    int price;

    public addFoodItemClass(){

    }

    public  addFoodItemClass(String name,int price){
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
