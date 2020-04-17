package test;

import com.mec.ip.configs.IPConfig;
import com.mec.ip.interfaces.impls.portfolio.CollectionPortfolio;
import com.mec.ip.objects.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Calendar;

public class TestPortfolio {

    CollectionPortfolio portfolio;
    ApplicationContext context;
    @Before
    public void init() {
        context = new AnnotationConfigApplicationContext(IPConfig.class);
        portfolio = new CollectionPortfolio();
    }

    private Stock getNewStock() {

        Stock stock = context.getBean("stock",Stock.class);
        stock.setDate(Calendar.getInstance().getTime());
        stock.setTicker("MU");
        stock.setCount(3);
        stock.setPurchasePrice(58);
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
