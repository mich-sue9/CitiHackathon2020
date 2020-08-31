package com.congyre.trade.service;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.repository.TradeRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
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
    public Trade addTrade (Trade trade){
        trade.setDateCreated(new Date());
       
        return repo.insert(trade);
    }
    public void deleteTradeById(ObjectId id){
        repo.deleteById(id);
    }

    public Optional<Trade> getTradeById(ObjectId id){
        return repo.findById(id);
    }

    public void cancelTrade(ObjectId tradeId){
        Optional<Trade> retrievedTrade = this.getTradeById(tradeId);
        Trade newTrade = retrievedTrade.get();
        String oldTicker = newTrade.getStockTicker();
        newTrade.setStockTicker("CANCELLED");
        repo.save(newTrade); 
        log.log(Level.INFO, "Trade status being updated from " + oldTicker + " to " + newTrade.getStockTicker());
    }

    public List<Trade> getTradesByTicker(String tickerName){
        return repo.customFindByStockTicker(tickerName);
    }

}