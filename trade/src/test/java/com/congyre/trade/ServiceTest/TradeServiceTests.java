package com.congyre.trade.ServiceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.PortfolioRepository;
import com.congyre.trade.repository.TradeRepository;
import com.congyre.trade.repository.UserRepository;
import com.congyre.trade.service.PortfolioService;
import com.congyre.trade.service.TradeService;
import com.congyre.trade.service.UserService;

import org.bson.types.ObjectId;
import org.junit.Test;

import org.junit.runner.RunWith;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TradeServiceTests.Config.class)
public class TradeServiceTests {

    private static ObjectId ID = new ObjectId("5f46a3d545bee629d17fd7b2");
    private static Trade trade2;
    private static String ticker = "AAPL";


    @Configuration
    public static class Config {

        @Bean
        public TradeRepository repo() {

           
            List<Trade> trades = new ArrayList<>();
            Trade trade = new Trade();
            trade.setId(ID);
            trade.setStockTicker(ticker);
            trades.add(trade);

            // setup the mock
            TradeRepository repo = mock(TradeRepository.class);
            when(repo.findAll()).thenReturn(trades);
            when(repo.insert(any(Trade.class))).thenReturn(trade2);
            when(repo.save(trade2)).thenReturn(trade2);
            when(repo.findById(ID)).thenReturn(Optional.of(trade));
            when(repo.customFindByStockTicker(ticker)).thenReturn(List.of(trade));
            return repo;
        }

        @Bean
        public TradeService service() {
            return new TradeService();
        }

        
        @Bean
        public UserService userService(){
            UserService service = mock(UserService.class);
            return service;
        }

        @Bean
        public UserRepository userRepository(){
            UserRepository repo = mock(UserRepository.class);
            return repo;
        }

        @Bean
        public PortfolioRepository portRepo(){
            PortfolioRepository repo = mock(PortfolioRepository.class);
            return repo;
        }
        @Bean
        public PortfolioService portService(){
            PortfolioService service = mock(PortfolioService.class);
            System.out.println("setup PortService");
            return service;
        }

    }

    @Autowired
    TradeService service;

    @Test
    public void testGetAllTrades(){
        Iterable<Trade> trades= service.getAllTrade();
        Stream<Trade> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(1L));
    }
    
    @Test
    public void testAddTrade(){
        Trade savedTrade=service.addTrade(new Trade(),ID);
        assertThat(savedTrade, equalTo(trade2));

    }
    

    @Test
    public void testGetTradeById(){
        Optional<Trade> getTrade = service.getTradeById(ID);
        assertThat(getTrade.isPresent(), equalTo(true));
        

    }
    @Test
    public void testGetTradesByTicker(){
        List<Trade> getTrade = service.getTradesByTicker(ticker);
        assertThat(getTrade.size(), equalTo(1));
    }


  

    
}