package com.mec.ip.configs;

import com.mec.ip.interfaces.impls.portfolio.CollectionPortfolio;
import com.mec.ip.interfaces.impls.portfolio.DBPortfolio;
import com.mec.ip.interfaces.impls.portfolio.HibernatePortfolio;
import com.mec.ip.interfaces.impls.strategy.FinvizStrategy;
import com.mec.ip.objects.Lang;
import com.mec.ip.objects.Stock;
import com.mec.ip.utils.LocaleManager;
import com.mec.ip.utils.tablecell.PaintGoalTableCell;
import com.mec.ip.utils.tablecell.PaintTableCellRedOrGreen;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class IPConfig {

    @Bean
    @Lazy
    public FinvizStrategy finvizStrategy() {
        return new FinvizStrategy(hibernatePortfolio());
    }

    @Bean
    @Lazy
    public HibernatePortfolio hibernatePortfolio() {
        return new HibernatePortfolio();
    }

    @Bean
    @Lazy
    public DBPortfolio dbPortfolio() {
        return new DBPortfolio();
    }

    @Bean
    @Lazy
    public CollectionPortfolio collectionPortfolio() {
        return new CollectionPortfolio();
    }

    @Bean
    public Lang langRU() {
        return new Lang(0, LocaleManager.RU_LOCALE);
    }

    @Bean
    public Lang langEN() {
        return new Lang(1, LocaleManager.EN_LOCALE);
    }

    @Bean
    @Scope("prototype")
    public Stock stock(){
        return new Stock();
    }

    @Bean
    @Scope("prototype")
    public PaintTableCellRedOrGreen paintTableCellRedOrGreen(){
        return new PaintTableCellRedOrGreen();
    }

    @Bean
    public PaintGoalTableCell paintGoalTableCell(){
        return new PaintGoalTableCell();
    }

}
