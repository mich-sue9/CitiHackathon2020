package com.congyre.trade.ControllerTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
import java.util.List;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Optional;
import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Stock;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.PortfolioRepository;
import com.congyre.trade.repository.TradeRepository;
import com.congyre.trade.rest.TradeController;
import com.congyre.trade.rest.PortfolioController;
import com.congyre.trade.service.PortfolioService;
import com.congyre.trade.service.TradeService;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=PortfolioControllerTest.Config.class)
public class PortfolioControllerTest {

    @SpringBootApplication(scanBasePackageClasses = {
        PortfolioController.class,
        PortfolioService.class
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config{}

    @Autowired 
    private TradeRepository repo;

    @Autowired
    private PortfolioRepository portRepo;


    @Autowired 
    private PortfolioController controller;

    public String testTradeID;
    public String testPortfolioID;
    
    @Before
    public void setUp(){
        repo.deleteAll();
        //portRepo.deleteAll();
        Trade trade = new Trade();
        Stock stock = new Stock();
        trade.setDateCreated(new Date());
        trade.setStockTicker("AAPL");
        trade.setQuantity(20);
        trade.setRequestPrice(20.00);
        trade.settStatus(TradeStatus.CREATED);
        Portfolio portfolio = new Portfolio();
    
        testTradeID = repo.save(trade).getId().toString();
        portfolio.addTradeIdToHistory(trade.getId());
        testPortfolioID = portRepo.save(portfolio).getId().toString();
        
        

    }


    @Test
    public void TestGetTradeHistory(){
        Iterable<Trade> trades = controller.getTradeHistory(testPortfolioID);
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(1L));
    }

    @Test
    public void TestGetPendingTrades(){
        Iterable<Trade> trades = controller.getPendingTrades(testPortfolioID);
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(0L));
    }

    @Test
    public void TestGetAllInPortfolio(){
        Portfolio portfolio = controller.getAllInPortfolio(testPortfolioID);
        assertThat(String.valueOf(portfolio.getTotalExpense()), equalTo("0.0"));

    }


}
