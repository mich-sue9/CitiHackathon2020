package com.congyre.trade.ControllerTest;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.PortfolioRepository;
import com.congyre.trade.repository.TradeRepository;
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
@ContextConfiguration(classes=TradeControllerTest.Config.class)

public class TradeControllerTest {

    @SpringBootApplication(scanBasePackageClasses = {
        TradeController.class,
        TradeService.class,
        PortfolioService.class,
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config{}
    
    @Autowired 
    private TradeController controller;

    @Autowired 
    private TradeRepository repo;

    @Autowired
    private PortfolioRepository portRepo;

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
    public void TestGetAllTrades(){}

    @Test
    public void TestGetTradeById(){}

    @Test
    public void TestGetTradesByTicker(){}

    @Test
    public void TestAddTrade(){
        //setup portfolio
        String portId = "5f5ecf766bf9793a7412d8f1";
        Portfolio testPort = new Portfolio();
        testPort.setId(new ObjectId(portId));

        //add portfolio into the repo
        portRepo.save(testPort);

        //test
        controller.addTrade(new Trade(), portId);
        Iterable<Trade> trades= repo.findAll();
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(2L));
    }


    @Test
    public void TestDeleteTradeById(){
        controller.deleteTradeById(testID);
        Iterable<Trade> trades= repo.findAll();
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(0L));

    }
    
    @Test
    public void TestCancelTrade(){
        controller.cancelTrade(testID);
        Trade test= repo.findById(new ObjectId(testID)).orElse(null);
        assertEquals(test.gettStatus(),TradeStatus.CANCELLED);
    }

}