package com.mec.ip.interfaces.impls.portfolio;

import com.mec.ip.interfaces.PortfolioAbstract;
import com.mec.ip.objects.Stock;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBPortfolio extends PortfolioAbstract {

    @Override
    public boolean add(Stock stock) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into stocks(date, ticker, title,count,purchase_price,price,pe,goal) values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            fillStatement(stock, statement);
            int result = statement.executeUpdate();
            if (result > 0) {
                int id = statement.getGeneratedKeys().getInt(1);
                stock.setId(id);
                stockList.add(stock);
                return true;
            }
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Stock stock) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("update  stocks set date=?, ticker=?, title=?,count=?,purchase_price=?,price=?,pe=?,goal=? where _id = ?", Statement.RETURN_GENERATED_KEYS)
        ) {
            fillStatement(stock, statement);
            statement.setInt(9, stock.getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void fillStatement(Stock stock, PreparedStatement statement) throws SQLException {
        statement.setString(1, stock.getDateStr());
        statement.setString(2, stock.getTicker());
        statement.setString(3, stock.getTitle());
        statement.setInt(4, stock.getCount());
        statement.setDouble(5, stock.getPrice());
        statement.setDouble(6, stock.getCurrentPrice());
        statement.setDouble(7, stock.getPE());
        statement.setDouble(8, stock.getGoal());
    }

    @Override
    public boolean delete(Stock stock) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            int result = statement.executeUpdate("delete from stocks where _id =" + stock.getId());
            if (result > 0) {
                stockList.remove(stock);
                return true;
            }
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public ObservableList<Stock> getStockList() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from stocks")) {

            stockList.clear();

            fillStockList(resultSet);

        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            System.out.println(e.getMessage());
        }
        recalculate();

        return stockList;
    }

    @Override
    public ObservableList<Stock> find(String text) {
        if (text.isEmpty())
            return getStockList();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from stocks where ticker like ? or title like ?")) {

            stockList.clear();

            String searchText = "%" + text + "%";
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet resultSet = statement.executeQuery();

            fillStockList(resultSet);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            System.out.println(e.getMessage());
        }
        recalculate();
        return stockList;
    }

    private void fillStockList(ResultSet resultSet) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setTitle(resultSet.getString("title"));
            stock.setTicker(resultSet.getString("ticker"));
            try {
                stock.setDate(dateFormat.parse(resultSet.getString("date")));
            } catch (ParseException e) {
                stock.setDate(new Date());
            }
            stock.setCount(resultSet.getInt("count"));
            stock.setPrice(resultSet.getDouble("purchase_price"));
            stock.setCurrentPrice(resultSet.getDouble("price"));
            stock.setPE(resultSet.getDouble("pe"));
            stock.setGoal(resultSet.getDouble("goal"));
            stock.setId(resultSet.getInt("_id"));
            stockList.add(stock);
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
        Connection conn = null;
        String url = "jdbc:sqlite:db/IP.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);
        return conn;
    }


}
