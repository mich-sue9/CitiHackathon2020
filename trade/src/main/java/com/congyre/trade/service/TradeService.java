package com.congyre.trade.service;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.TradeRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {
    private static Logger log = Logger.getLogger(TradeService.class.getName());

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

    public void cancelTrade(ObjectId tradeId){
        Optional<Trade> retrievedTrade = this.getTradeById(tradeId);
        if (!(retrievedTrade.isPresent())){
            log.log(Level.WARNING, "This trade does not exist");
        } else {
            Trade newTrade = retrievedTrade.get();
            String oldTicker = newTrade.getStockTicker();
            newTrade.setStockTicker("CANCELLED");
            repo.save(newTrade); 
            log.log(Level.INFO, "Trade status being updated from " + oldTicker + " to " + newTrade.getStockTicker());
        }
    }

    public Iterable<Trade> getTradeByTicker(String tickerName){
        return null;
    }

}