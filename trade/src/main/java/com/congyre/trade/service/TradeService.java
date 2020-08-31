package com.congyre.trade.service;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.TradeRepository;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    private TradeRepository repo;


    public Collection<Trade> getAllTrade(){
        //TODO:ADD 
        return repo.findAll();
    }
    public void addTrade (Trade trade){
        
        //TODO:ADD 
        repo.insert(trade);
    }
    public void deleteTradeById(ObjectId id){

        //TODO:ADD 
        return;


    }
    public Optional<Trade> getTradeById(ObjectId id){
        //TODO:ADD
        return null;
    }
    public void updateTrade(Trade trade){
        //TODO:ADD


        return;
    }

    public Collection<Trade> returnTrades(String name){
        //
        return null;
    }

}