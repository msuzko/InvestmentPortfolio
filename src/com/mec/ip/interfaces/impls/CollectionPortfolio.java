package com.mec.ip.interfaces.impls;

import com.mec.ip.interfaces.Portfolio;
import com.mec.ip.objects.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class CollectionPortfolio implements Portfolio {

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();

    @Override
    public void add(Stock stock) {
        stockList.add(stock);
        recalculate();
    }

    @Override
    public void update(Stock stock) {
        recalculate();
    }

    @Override
    public void delete(Stock stock) {
        stockList.remove(stock);
        recalculate();
    }

    public ObservableList<Stock> getStockList() {
        return stockList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Stock stock : stockList) {
            builder.append(stock).append("\n");
        }
        return builder.toString();
    }

    public void print() {
        System.out.println(this);
    }


    public void fillTestData() {
        stockList.add(new Stock(getDate(-10), "VEON", 900, 2.447, 2));
        stockList.add(new Stock(getDate(-25), "F", 364, 8.69, 0.8));
        stockList.add(new Stock(getDate(-13), "AA", 70, 17.15, 0.3));
        stockList.add(new Stock(getDate(-19), "M", 100, 19.7, 0.85));
        stockList.add(new Stock(getDate(-3), "GILD", 8, 63.65, 1.4));
        stockList.add(new Stock(getDate(-48), "V", 2, 177.3, 0.7));
        stockList.add(new Stock(getDate(-21), "AXP", 3, 117.94, 0.2));
       // recalculate();
    }

    public void recalculate() {
        double sum = getSum();
        stockList.forEach(stock ->
                stock.setWeight(round(stock.getAmount() / sum * 100, 2)));
    }

    public double getSum() {
        return stockList.stream().mapToDouble(Stock::getAmount).sum();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Date getDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

}
