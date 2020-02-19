package com.mec.ip.interfaces;

import com.mec.ip.objects.Stock;

public interface Portfolio {

    void add(Stock stock);

    void update(Stock stock);

    void delete(Stock stock);

}
