package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.PortfolioRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.congyre.trade.repository.TradeRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private static Logger log = Logger.getLogger(PortfolioService.class.getName());

    @Autowired
    private PortfolioRepository repo;
    
    @Autowired
    private TradeService tradeService;


    public Optional<Portfolio> getportfolio(ObjectId userId){
        return repo.findByUserId(userId);
    }

    public Optional<Set> getTradeHistory(ObjectId userId){
        Optional<Portfolio> retrivePortfolio = repo.findByUserId(userId);
        Portfolio portfolio = retrivePortfolio.get();
        return portfolio.getHistory();
    }

    public void addTrade(Trade trade){
    }

    
    @Scheduled(fixedDelay = 1000)
    public void scheduleUpdateOutstandingTrade() {
        log.info("start the interval call to update the outsanding trade for all the portfolios in dbs");

    }

}