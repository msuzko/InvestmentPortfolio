package com.mec.ip.interfaces;

import com.mec.ip.objects.Stock;

import java.util.List;

public interface Strategy {

    void updateDataInList(List<Stock> stockList);

    void updateStock(Stock stock);
}
