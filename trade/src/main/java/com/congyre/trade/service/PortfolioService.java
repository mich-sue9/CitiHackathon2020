package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.PortfolioRepository;

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

    public void getportfolio(ObjectId userId){

    }

    public void addTrade(Trade trade){
        
    }

}