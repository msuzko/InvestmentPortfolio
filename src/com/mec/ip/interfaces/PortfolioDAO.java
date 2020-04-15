package com.mec.ip.interfaces;

import com.mec.ip.objects.Stock;
import javafx.collections.ObservableList;

public interface PortfolioDAO {

    boolean add(Stock stock);

    void update(Stock stock);

    void delete(Stock stock);

    ObservableList<Stock> getStockList();

    ObservableList<Stock> find(String text);

    double getCurrentSum();

    double getPurchaseSum();

    void updateWeight();

}
