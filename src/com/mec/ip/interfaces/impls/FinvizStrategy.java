package com.mec.ip.interfaces.impls;

import com.mec.ip.interfaces.PortfolioDAO;
import com.mec.ip.interfaces.Strategy;
import com.mec.ip.objects.Stock;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinvizStrategy implements Strategy {
    private static final String URL_FORMAT = "https://finviz.com/quote.ashx?t=%s";
    private PortfolioDAO portfolio;

    public FinvizStrategy(PortfolioDAO portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public List<Stock> updateDataInList(List<Stock> stockList) {
        List<Stock> list = new ArrayList<>();
        for (Stock stock : stockList) {
            updateStock(stock);
            list.add(stock);
        }
        return list;
    }

    @Override
    public void updateStock(Stock stock) {
        try {
            Document doc = getDocument(stock.getTicker());
            Element element = doc.getElementsByClass("snapshot-table2").get(0);
            Elements rows = element.select("tr");
            if (rows.size() == 0)
                return;
            double pe = stock.getPE();
            double goal = stock.getGoal();
            String title = stock.getTitle();
            fillStock(stock, rows, doc);
            if (stock.getPE() != pe || stock.getGoal() != goal || !stock.getTitle().equals(title))
                portfolio.update(stock);
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404)
                System.out.println("Акция с тикером " + stock.getTicker() + " не найдена");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getTitle(Document doc) {
        Element element = doc.getElementsByClass("fullview-title").get(0);
        Elements rows = element.select("tr");
        if (rows.size() < 2)
            return "";
        Elements columns = rows.get(1).select("td");
        if (columns.size() == 0)
            return "";
        return columns.get(0).text();
    }

    private void fillStock(Stock stock, Elements rows, Document doc) {
        Map<String, String> map = getIndicators(rows);
        stock.setPE(getDoubleIndicator(map, "P/E"));
        stock.setGoal(getDoubleIndicator(map, "Target Price"));
        stock.setCurrentPrice(getDoubleIndicator(map, "Price"));
        stock.setTitle(getTitle(doc));
    }

    private double getDoubleIndicator(Map<String, String> map, String s) {
        try {
            return Double.parseDouble(map.get(s));
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    private Map<String, String> getIndicators(Elements rows) {
        Map<String, String> map = new HashMap<>();
        int i = 0;
        for (Element row : rows) {
            Elements columns = row.select("td");
            String key = "";
            for (Element column : columns) {
                if (++i % 2 != 0)
                    key = column.text();
                else
                    map.put(key, column.text());
            }
        }
        return map;
    }

    public Document getDocument(String searchString) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36")
                .referrer("").get();
    }
}
