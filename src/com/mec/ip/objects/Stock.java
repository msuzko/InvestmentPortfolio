package com.mec.ip.objects;

import com.mec.ip.utils.Math;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stock {

    private Date date;
    private SimpleStringProperty ticker = new SimpleStringProperty("");
    private SimpleStringProperty title = new SimpleStringProperty("");
    private SimpleIntegerProperty count = new SimpleIntegerProperty();
    private SimpleDoubleProperty price = new SimpleDoubleProperty();
    private SimpleDoubleProperty amount = new SimpleDoubleProperty();
    private SimpleStringProperty dateStr = new SimpleStringProperty("");
    private SimpleDoubleProperty currentPrice = new SimpleDoubleProperty();
    private SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private SimpleDoubleProperty pl = new SimpleDoubleProperty();
    private SimpleDoubleProperty plPercent = new SimpleDoubleProperty();
    private SimpleDoubleProperty pe = new SimpleDoubleProperty();
    private SimpleDoubleProperty goal = new SimpleDoubleProperty();

    private final static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private int id;

    public Stock() {
    }

    public Stock(Date date, String ticker, int count, double price, double commission) {
        setDate(date);
        this.ticker.set(ticker);
        this.count.set(count);
        this.price.set(price);
        this.amount.set(count * currentPrice.get());
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

    public double getPurchaseAmount() {
        return count.get() * price.get();
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

    public void setPE(double pe) {
        this.pe.set(pe);
    }

    public void setGoal(double goal) {
        this.goal.set(goal);
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice.set(currentPrice);
        calculateAmount();
    }


    public void setPL(double pl) {
        this.pl.set(pl);
    }

    public void setPlPercent(double plPercent) {
        this.plPercent.set(plPercent);
    }

    public void setCount(int count) {
        this.count.set(count);
        calculateAmount();
    }

    private void calculateAmount() {
        this.amount.set(Math.round(this.count.get() * this.currentPrice.get(), 2));
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "date=" + dateStr.get() +
                ", ticker='" + ticker.get() + '\'' +
                ", count=" + count.get() +
                ", price=" + price.get() +
                ", cur. price=" + currentPrice.get() +
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

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public SimpleStringProperty dateStrProperty() {
        return dateStr;
    }

    public SimpleDoubleProperty currentPriceProperty() {
        return currentPrice;
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

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public double getGoal() {
        return goal.get();
    }

    public double getPE() {
        return pe.get();
    }

    public double getCurrentPrice() {
        return currentPrice.get();
    }

    public double getPL() {
        return pl.get();
    }

    public double getPlPercent() {
        return plPercent.get();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

