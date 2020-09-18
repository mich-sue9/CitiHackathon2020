package com.congyre.trade.ServiceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringRunner;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.PortfolioRepository;
import com.congyre.trade.repository.TradeRepository;
import com.congyre.trade.service.PortfolioService;
import com.congyre.trade.service.TradeService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= PortfolioSchedulerIntegratedTest.Config.class)
@EnableScheduling
public class PortfolioSchedulerIntegratedTest {
    @SpringBootApplication(scanBasePackageClasses={
        PortfolioService.class,
        TradeService.class
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")
    public static class Config {

    }



    public final ObjectId USER_ID = new ObjectId("5f46a3d545bee629d17fd7b2");
    public final ObjectId PORD_ID = new ObjectId("4546a3d545bee629d17fd7b2");

    @Autowired
    private PortfolioService service;

    @Autowired
    private TradeService tradeService;
    @Autowired
    private PortfolioRepository repo;
    @Autowired
    private TradeRepository tradeRepo;

    @Before
    public void setUp() {
        repo.deleteAll();
        tradeRepo.deleteAll();
        // create a portfolio
        // Portfolio port = new Portfolio();
        Portfolio port = new Portfolio();
        port.setId(PORD_ID);
        port.setCashOnHand(1000000);
        service.addPortfolio(USER_ID, port);
        service.addStock("AAPL", 400, PORD_ID);

        // add some trades with FILLED or REJECTED status
        // and these stocks should be removed by scheduler
        ObjectId tradeId1 = new ObjectId("1a46a3d545bee629d17fd7b2");
        ObjectId tradeId2 = new ObjectId("2b46a3d545bee629d17fd7b2");

        Trade trade1 = new Trade();
        trade1.settStatus(TradeStatus.FILLED);
        trade1.setId(tradeId1);
        trade1.setStockTicker("AAPL");
        trade1.setQuantity(2);
        trade1.setRequestPrice(123.45);

        Trade trade2 = new Trade();
        trade2.settStatus(TradeStatus.REJECTED);
        trade2.setId(tradeId2);
        trade2.setStockTicker("AAPL");
        trade2.setQuantity(3);
        trade2.setRequestPrice(123.45);

        tradeService.addTrade(trade1, PORD_ID);
        tradeService.addTrade(trade2, PORD_ID);

    }

    @Test
    public void testScheduler() {
        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertThat(service.getPendingTrades(PORD_ID).isEmpty(), equalTo(true));


    }




    
}
