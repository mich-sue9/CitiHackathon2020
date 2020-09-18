package com.congyre.trade.service;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.Trade.TradeStatus;
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

    @Autowired 
    private PortfolioService portfolio;

    public Collection<Trade> getAllTrade(){
        return repo.findAll();
    }

    /***
     * Submits a request to trade
     * A new trade will be submitted to the Trade repo and added to a portfolio's history & outstanding list 
     * @param trade The new trade that was created
     * @param porfolioId The id of the portfolio that the trade was created for 
     * @return Trade that was added 
     */
    public Trade addTrade (Trade trade, ObjectId porfolioId){
        trade.setDateCreated(new Date());
        Trade newTrade = repo.insert(trade);
        portfolio.addTrade(newTrade.getId(), porfolioId);
        return newTrade;
    }

    /***
     *  Delete a trade from the trade repository
     * @param id used to identify which trade to delete
     */
    public void deleteTradeById(ObjectId id){
        repo.deleteById(id);
    }

    /***
     * Retrieves the trade with a given id
     * @param id used to identify which trade to retrieve
     * @return
     */
    public Optional<Trade> getTradeById(ObjectId id){
        return repo.findById(id);
    }

    /***
     * Changes a trade's status to cancelled
     * @param tradeId id of trade to cancel
     */
    public void cancelTrade(ObjectId tradeId){
        Optional<Trade> retrievedTrade = this.getTradeById(tradeId);
        Trade newTrade = retrievedTrade.get();
        TradeStatus oldTicker = newTrade.gettStatus();
        newTrade.settStatus(TradeStatus.CANCELLED);
        repo.save(newTrade); 
        log.log(Level.INFO, "Trade status being updated from " + oldTicker + " to " + newTrade.getStockTicker());
    }


    /**
     * Retrieve all trades related to a certain ticker
     * @param tickerName name of ticker to retrieve list of 
     * @return
     */
    public List<Trade> getTradesByTicker(String tickerName){
        return repo.customFindByStockTicker(tickerName);
    }

}