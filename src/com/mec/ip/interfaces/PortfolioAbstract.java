package com.mec.ip.interfaces;

import com.mec.ip.objects.Stock;
import com.mec.ip.utils.Math;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.context.ApplicationContext;

public abstract class PortfolioAbstract implements PortfolioDAO {

    protected ObservableList<Stock> stockList = FXCollections.observableArrayList();

    protected ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void updateWeight() {
        double sum = getCurrentSum();
        stockList.forEach(stock ->
                stock.setWeight(Math.round(stock.getAmount() / sum * 100, 2))
        );
    }

    @Override
    public double getPurchaseSum() {
        return stockList.stream().mapToDouble(Stock::getPurchaseAmount).sum();
    }

    @Override
    public double getCurrentSum() {
        return stockList.stream().mapToDouble(Stock::getAmount).sum();
    }


}
