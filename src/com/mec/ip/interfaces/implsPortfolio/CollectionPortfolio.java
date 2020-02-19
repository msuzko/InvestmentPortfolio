package com.mec.ip.interfaces.implsPortfolio;

import com.mec.ip.interfaces.Portfolio;
import com.mec.ip.objects.Stock;

import java.util.ArrayList;
import java.util.List;

public class CollectionPortfolio implements Portfolio {

    private List<Stock> stockList = new ArrayList<>();

    @Override
    public void add(Stock stock) {
        stockList.add(stock);
    }

    @Override
    public void update(Stock stock) {

    }

    @Override
    public void delete(Stock stock) {
        stockList.remove(stock);
    }

    public List<Stock> getStockList() {
        return stockList;
    }
}
