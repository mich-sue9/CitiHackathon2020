package com.congyre.trade.service;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.TradeRepository;

import java.util.Collection;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @Autowired
    private TradeRepository repo;


    public Collection<Trade> getAllTrade(){
        return repo.findAll();
    }
    public void addTrade (Trade trade){
        repo.insert(trade);
    }
    public void deleteTradeById(ObjectId id){
        repo.deleteById(id);
    }

    public Optional<Trade> getTradeById(ObjectId id){
        return repo.findById(id);
    }

    public void updateTrade(Trade trade){
        //TODO:ADD
        return;
    }

    public Collection<Trade> returnTrades(String name){
        //
        return null;
    }

    public Iterable<Trade> getTradeByTicker(String tickerName){
        return null;
    }

}