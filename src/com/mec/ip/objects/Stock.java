package com.mec.ip.objects;

import com.mec.ip.utils.Math;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity(name = "stocks")
public class Stock {

    private SimpleStringProperty ticker = new SimpleStringProperty("");
    private SimpleStringProperty title = new SimpleStringProperty("");
    private SimpleIntegerProperty count = new SimpleIntegerProperty();
    private SimpleDoubleProperty purchasePrice = new SimpleDoubleProperty();
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

    public Stock(Date date, String ticker, int count, double purchasePrice) {
        this.dateStr = new SimpleStringProperty(format.format(date));
        this.ticker = new SimpleStringProperty(ticker);
        this.count = new SimpleIntegerProperty(count);
        this.purchasePrice = new SimpleDoubleProperty(purchasePrice);
        this.amount = new SimpleDoubleProperty(count * currentPrice.get());
    }

    public void setDate(Date date) {
        this.dateStr = new SimpleStringProperty(format.format(date));
    }

    @Id
    @Column(name = "_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date", nullable = false, length = -1)
    public String getDateStr() {
        return dateStr.get();
    }

    public void setDateStr(String dateStr) {
        this.dateStr.set(dateStr);
    }

    @Basic
    @Column(name = "ticker", nullable = false, length = -1)
    public String getTicker() {
        return ticker.get();
    }

    public void setTicker(String ticker) {
        this.ticker.set(ticker);
    }

    @Basic
    @Column(name = "title", length = -1)
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @Basic
    @Column(name = "count", nullable = false)
    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
        calculateAmount();
    }

    @Basic
    @Column(name = "purchase_price", nullable = false)
    public double getPurchasePrice() {
        return purchasePrice.get();
    }

    public void setPurchasePrice(double price) {
        this.purchasePrice.set(price);
        calculatePL();
    }

    @Basic
    @Column(name = "price")
    public double getCurrentPrice() {
        return currentPrice.get();
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice.set(currentPrice);
        calculateAmount();
        calculatePL();
    }

    private void calculatePL() {
        if (getCurrentPrice() != 0 && getPurchasePrice() != 0) {
            setPL(Math.round(getCount() * (getCurrentPrice() - getPurchasePrice()), 2));
            setPlPercent(Math.round(getAmount() / getPurchaseAmount() * 100 - 100, 2));
        }
    }

    @Basic
    @Column(name = "pe")
    public double getPE() {
        return pe.get();
    }

    public void setPE(double pe) {
        this.pe.set(pe);
    }

    @Basic
    @Column(name = "goal")
    public double getGoal() {
        return goal.get();
    }

    public void setGoal(double goal) {
        this.goal.set(goal);
    }

    @Transient
    public double getAmount() {
        return amount.get();
    }

    @Transient
    public double getPL() {
        return pl.get();
    }

    @Transient
    public double getPlPercent() {
        return plPercent.get();
    }

    @Transient
    public double getPurchaseAmount() {
        return count.get() * purchasePrice.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public void setPL(double pl) {
        this.pl.set(pl);
    }

    public void setPlPercent(double plPercent) {
        this.plPercent.set(plPercent);
    }

    private void calculateAmount() {
        this.amount.set(Math.round(this.count.get() * this.currentPrice.get(), 2));
    }

    @Override
    public String toString() {
        return "Stock{" +
                "date=" + dateStr.get() +
                ", ticker='" + ticker.get() + '\'' +
                ", count=" + count.get() +
                ", purc. price=" + purchasePrice.get() +
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
        return purchasePrice;
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

    public SimpleStringProperty titleProperty() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return getId() == stock.getId() &&
                Objects.equals(getTicker(), stock.getTicker()) &&
                Objects.equals(getTitle(), stock.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicker(), getTitle(), getId());
    }
}

