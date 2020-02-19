package test;

import com.mec.ip.interfaces.Portfolio;
import com.mec.ip.interfaces.implsPortfolio.CollectionPortfolio;
import com.mec.ip.objects.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class testPortfolio {

    CollectionPortfolio portfolio;
    Stock stock1, stock2;

    @Before
    public void init() {
        portfolio = new CollectionPortfolio();

        stock1 = getNewStock();
        stock2 = getNewStock();

        portfolio.add(stock1);
        portfolio.add(stock2);

    }

    private Stock getNewStock() {
        Stock stock = new Stock();
        stock.setDate(Calendar.getInstance());
        stock.setTiker("MU");
        stock.setCount(3);
        stock.setCost(58);
        stock.setCommission(0.3);
        return stock;
    }

    @Test
    public void testAdd() {
        portfolio.add(getNewStock());
        Assert.assertEquals(3, portfolio.getStockList().size());
    }

    @Test
    public void testUpdate() {
        portfolio.update(stock2);
        Assert.assertEquals(2, portfolio.getStockList().size());
    }

    @Test
    public void testDelete() {
        portfolio.delete(stock1);
        Assert.assertEquals(1, portfolio.getStockList().size());
    }

}
