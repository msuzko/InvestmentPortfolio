package com.mec.ip.objects;

import java.util.Calendar;

public class Stock {

    private Calendar date;
    private String tiker;
    private int count;
    private double cost;
    private double commission;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getTiker() {
        return tiker;
    }

    public void setTiker(String tiker) {
        this.tiker = tiker;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
