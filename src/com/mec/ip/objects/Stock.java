package com.mec.ip.objects;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stock {

    private Date date;
    private SimpleStringProperty ticker = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleIntegerProperty count = new SimpleIntegerProperty();
    private SimpleDoubleProperty cost = new SimpleDoubleProperty();
    private SimpleDoubleProperty amount = new SimpleDoubleProperty();
    private SimpleStringProperty dateStr = new SimpleStringProperty("");
    private SimpleDoubleProperty currentCost = new SimpleDoubleProperty();
    private SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private SimpleDoubleProperty pl = new SimpleDoubleProperty();
    private SimpleDoubleProperty plPercent = new SimpleDoubleProperty();
    private SimpleDoubleProperty pe = new SimpleDoubleProperty();
    private SimpleDoubleProperty goal = new SimpleDoubleProperty();

    private final static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Stock() {
    }

    public Stock(Date date, String ticker, int count, double cost, double commission) {
        setDate(date);
        this.ticker.set(ticker);
        this.count.set(count);
        this.cost.set(cost);
        this.amount.set(count * cost);
    }

    public void setDate(Date date) {
        this.date = date;
        this.dateStr.set(format.format(date));
    }

    public String getDateStr() {
        return dateStr.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public String getTicker() {
        return ticker.get();
    }

    public void setTicker(String ticker) {
        this.ticker.set(ticker);
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
        calculateAmount();
    }

    private void calculateAmount() {
        this.amount.set(this.count.get() * this.cost.get());
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
        calculateAmount();
    }

    @Override
    public String toString() {
        return "Stock{" +
                "date=" + dateStr.get() +
                ", ticker='" + ticker.get() + '\'' +
                ", count=" + count.get() +
                ", cost=" + cost.get() +
                ", cur. cost=" + currentCost.get() +
                ", amount=" + amount.get() +
                ", weight=" + weight.get() +
                ", P/L=" + pl.get() +
                ", P/L%=" + plPercent.get() +
                ", P/E=" + pe.get() +
                ", goal=" + goal.get() +
                '}';
    }

    public SimpleStringProperty tickerProperty() {
        return ticker;
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public SimpleStringProperty dateStrProperty() {
        return dateStr;
    }

    public SimpleDoubleProperty currentCostProperty() {
        return currentCost;
    }

    public SimpleDoubleProperty weightProperty() {
        return weight;
    }

    public SimpleDoubleProperty plProperty() {
        return pl;
    }

    public SimpleDoubleProperty plPercentProperty() {
        return plPercent;
    }

    public SimpleDoubleProperty peProperty() {
        return pe;
    }

    public SimpleDoubleProperty goalProperty() {
        return goal;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
