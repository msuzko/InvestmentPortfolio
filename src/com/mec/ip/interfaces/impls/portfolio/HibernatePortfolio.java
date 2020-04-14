package com.mec.ip.interfaces.impls.portfolio;

import com.mec.ip.interfaces.PortfolioAbstract;
import com.mec.ip.objects.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class HibernatePortfolio extends PortfolioAbstract {

    private static SessionFactory sessionFactory;

    public HibernatePortfolio() {
        sessionFactory = getSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    @Override
    public boolean add(Stock stock) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(stock);
        session.flush();
        session.close();
        stockList.add(stock);
        return true;
    }

    @Override
    public boolean update(Stock stock) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(stock);
        session.flush();
        session.close();
        return true;
    }

    @Override
    public boolean delete(Stock stock) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(stock);
        session.flush();
        session.close();
        stockList.remove(stock);
        return true;
    }

    @Override
    public ObservableList<Stock> getStockList() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Stock> criteria = builder.createQuery(Stock.class);

        Root<Stock> myStockRoot = criteria.from(Stock.class);
        criteria.select(myStockRoot);

        fillStockList(session, criteria);

        session.close();
        return stockList;
    }

    @Override
    public ObservableList<Stock> find(String text) {
        if (text.isEmpty())
            return getStockList();
        String searchText = "%" + text + "%";
        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Stock> criteria = builder.createQuery(Stock.class);

        Root<Stock> myStockRoot = criteria.from(Stock.class);
        Predicate likeRestriction = builder.and(
                builder.like(myStockRoot.get("title"), searchText),
                builder.like(myStockRoot.get("ticker"), searchText)
        );
        criteria.select(myStockRoot).where(likeRestriction);

        fillStockList(session, criteria);
        session.close();
        return stockList;
    }

    private void fillStockList(Session session, CriteriaQuery<Stock> criteria) {
        TypedQuery<Stock> query = session.createQuery(criteria);
        ObservableList<Stock> list = FXCollections.observableArrayList();
        list.addAll(query.getResultList());
        for (Stock stock: list)
            if (!stockList.contains(stock))
                stockList.add(stock);
        stockList.removeIf(stock -> !list.contains(stock));
    }


}
