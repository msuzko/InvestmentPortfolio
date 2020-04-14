package test;

import com.mec.ip.interfaces.impls.portfolio.CollectionPortfolio;
import com.mec.ip.objects.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class TestPortfolio {

    CollectionPortfolio portfolio;

    @Before
    public void init() {
        portfolio = new CollectionPortfolio();
    }

    private Stock getNewStock() {
        Stock stock = new Stock();
        stock.setDate(Calendar.getInstance().getTime());
        stock.setTicker("MU");
        stock.setCount(3);
        stock.setPrice(58);
        return stock;
    }

    @Test
    public void testAdd() {
        portfolio.add(getNewStock());
        Assert.assertEquals(8, portfolio.getStockList().size());
    }

    @Test
    public void testUpdate() {
        portfolio.update(portfolio.getStockList().get(0));
        Assert.assertEquals(7, portfolio.getStockList().size());
    }

    @Test
    public void testDelete() {
        portfolio.delete(portfolio.getStockList().get(0));
        Assert.assertEquals(6, portfolio.getStockList().size());
    }

    @Test
    public void testPrint(){
        portfolio.print();
    }

}
