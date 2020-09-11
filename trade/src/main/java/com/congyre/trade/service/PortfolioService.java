package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.repository.PortfolioRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.congyre.trade.repository.TradeRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private static Logger log = Logger.getLogger(PortfolioService.class.getName());

    @Autowired
    private PortfolioRepository repo;

    public Optional<Portfolio> getportfolio(ObjectId userId){
        return repo.findByUserId(userId);
    }

    public Optional<Set> getTradeHistory(ObjectId userId){
        Optional<Portfolio> retrivePortfolio = repo.findByUserId(userId);
        Portfolio portfolio = retrivePortfolio.get();
        return portfolio.getHistory();
    }

}