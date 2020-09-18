package com.congyre.trade.ControllerTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


import java.util.HashMap;
import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Stock;
import com.congyre.trade.repository.PortfolioRepository;
import com.congyre.trade.rest.PortfolioController;
import com.congyre.trade.rest.TradeController;
import com.congyre.trade.service.PortfolioService;
import com.congyre.trade.service.TradeService;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PortfolioStocksControllerTest.Config.class)

public class PortfolioStocksControllerTest {

    @SpringBootApplication(scanBasePackageClasses = { TradeController.class, TradeService.class,
            PortfolioService.class, })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config {
    }

    @Autowired
    private PortfolioController controller;

    @Autowired
    private PortfolioService service;

    @Autowired
    private PortfolioRepository repo;

    public String portId;

    private String userId;

    private HashMap<String, Stock> stocks;

    private Portfolio testPort;


    @Before
    public void setUp(){
        // clean the test db
        repo.deleteAll();

        //setup a empty portfolio
        portId = "5f46a3d545bee629d17fd7b2";
        testPort = new Portfolio();
        testPort.setId(new ObjectId(portId));
    }


    @Test
    public void testAddPortfolio(){
        //add empty portfolio to a user
        userId = "2346a3d545bee629d17fd7b2";
        controller.addPortfolio(userId, testPort);
        
        //test 
        Portfolio getPortfolio = service.getportfolio(new ObjectId(portId));
        assertThat(getPortfolio.getId(), equalTo(new ObjectId(portId)));
    }


    @Test
    public void testAddStock(){
        //add empty portfolio to a user
        userId = "2346a3d545bee629d17fd7b2";
        controller.addPortfolio(userId, testPort);

        //add stock
        controller.addStock("AAPL", portId, 23);

        //test a stock is added into portfoio inventory
        stocks = service.getStocks(new ObjectId(portId));
        assertThat(stocks.size(), equalTo(1));

    }

    @Test
    public void testAddRemoveStock(){
        //add empty portfolio to a user
        userId = "2346a3d545bee629d17fd7b2";
        controller.addPortfolio(userId, testPort);

        // add then remove stock
        controller.addStock("AAPL", portId, 23);
        controller.removeStock("AAPL", portId, 10);
        controller.removeStock("AAPL", portId, 13);

        //test a correct amount of stocks are removed
        stocks = service.getStocks(new ObjectId(portId));
        assertThat(stocks.size(), equalTo(0));
    }



}