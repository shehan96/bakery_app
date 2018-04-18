package com.adelmotechnology.bakery_app;

/**
 * Created by IMPERIUM on 4/17/2018.
 */

public class salesDetailsClass {
    String month,name;
    int date,total;

    public salesDetailsClass() {
    }

    public salesDetailsClass(String month, String name, int date, int total) {
        this.month = month;
        this.name = name;
        this.date = date;
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public int getTotal() { return total; }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
