package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.PortfolioRepository;

import java.util.HashSet;
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

    public Optional<Portfolio> getportfolio(ObjectId id) {
        return repo.findById(id);
    }

<<<<<<< HEAD
    public void addPortfolio

    public Optional<Set> getTradeHistory(ObjectId userId){
        Optional<Portfolio> retrivePortfolio = repo.findByUserId(userId);
=======
    public HashSet<Trade> getTradeHistory(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);
>>>>>>> b5c95c1a26f1735a2f49336bc3994f7a7eb8c9df
        Portfolio portfolio = retrivePortfolio.get();
        return portfolio.getHistory(); // can return null
    }

    public void addTrade(Trade trade){
    }

    
    @Scheduled(fixedDelay = 1000)
    public void scheduleUpdateOutstandingTrade() {
        log.info("start the interval call to update the outsanding trade for all the portfolios in dbs");
        List<Portfolio> portList = repo.findAll();

        

    }

}