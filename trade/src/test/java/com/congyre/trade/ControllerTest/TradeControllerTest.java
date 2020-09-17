package com.congyre.trade.ControllerTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.TradeRepository;
import com.congyre.trade.rest.TradeController;
import com.congyre.trade.service.TradeService;

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
@ContextConfiguration(classes=TradeControllerTest.Config.class)

public class TradeControllerTest {

    @SpringBootApplication(scanBasePackageClasses = {
        TradeController.class,
        TradeService.class
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config{}
    
    @Autowired 
    private TradeController controller;

    @Autowired 
    private TradeRepository repo;

    public String testID;

    @Before
    public void setUp(){
        repo.deleteAll();
        Trade trade = new Trade();
        trade.setDateCreated(new Date());
        trade.setStockTicker("AAPL");
        trade.setQuantity(20);
        trade.setRequestPrice(20.20);
        trade.settStatus(TradeStatus.CREATED);

        testID = repo.save(trade).getId().toString();
    }

    @Test
    public void TestGetAllTrades(){
        Iterable<Trade> trades = controller.getAllTrades();
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(1L));
    }

    @Test
    public void TestGetTradeById(){
        Optional<Trade> trade = controller.getTradeById(testID);
        assertThat(trade.isPresent(), equalTo(true));
    }

    @Test
    public void TestGetTradesByTicker(){
        List<Trade> trades = controller.getTradesByTicker("AAPL");
        assertThat(trades.size(), equalTo(1));
    }

    @Test
    public void TestAddTrade(){}

    @Test
    public void TestDeleteTradeById(){}
    
    @Test
    public void TestCancelTrade(){}
    
    @Test
    public void TestCheckTradeIdExists(){

    }
    
}