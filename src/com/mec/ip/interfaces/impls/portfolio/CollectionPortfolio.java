package com.mec.ip.interfaces.impls.portfolio;

import com.mec.ip.interfaces.PortfolioAbstract;
import com.mec.ip.objects.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Calendar;
import java.util.Date;

public class CollectionPortfolio extends PortfolioAbstract {

    private ObservableList<Stock> backupList = FXCollections.observableArrayList();;


    public CollectionPortfolio() {
        fillTestData();
    }

    @Override
    public boolean add(Stock stock) {
        stockList.add(stock);
        backupList.add(stock);
        updateWeight();
        return true;
    }

    @Override
    public boolean update(Stock stock) {
        updateWeight();
        return true;
    }

    @Override
    public boolean delete(Stock stock) {
        stockList.remove(stock);
        backupList.remove(stock);
        updateWeight();
        return true;
    }

    @Override
    public ObservableList<Stock> find(String text) {
        stockList.clear();

        for (Stock stock : backupList) {
            if (stock.getTicker().toLowerCase().contains(text.toLowerCase()) ||
                    stock.getTitle().toLowerCase().contains(text.toLowerCase())) {
                stockList.add(stock);
            }
        }
        updateWeight();
        return stockList;
    }

    @Override
    public ObservableList<Stock> getStockList() {
        backupList = FXCollections.observableArrayList();
        backupList.addAll(stockList);
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


    private void fillTestData() {
        stockList.add(new Stock(getDate(-10), "VEON", 900, 2.447));
        stockList.add(new Stock(getDate(-25), "F", 364, 8.69));
        stockList.add(new Stock(getDate(-13), "AA", 70, 17.15));
        stockList.add(new Stock(getDate(-19), "M", 100, 19.7));
        stockList.add(new Stock(getDate(-3), "GILD", 8, 63.65));
        stockList.add(new Stock(getDate(-48), "V", 2, 177.3));
        stockList.add(new Stock(getDate(-21), "AXP", 3, 117.94));
        // recalculate();
    }

    private Date getDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

}
