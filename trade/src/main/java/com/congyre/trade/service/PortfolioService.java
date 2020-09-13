package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Stock;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.User;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.PortfolioRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.lang.Exception;

import org.springframework.web.server.ResponseStatusException;

@Service
public class PortfolioService {
    private static Logger log = Logger.getLogger(PortfolioService.class.getName());

    @Autowired
    private PortfolioRepository repo;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;


    public Optional<Portfolio> getportfolio(ObjectId id) {
        return repo.findById(id);
    }

    public List<Trade> getTradeHistory(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);
        Portfolio portfolio = retrivePortfolio.get(); // can get null
        // want to add null portfolio exception handling?

        // get historical trades
        List<ObjectId> historicalIds = portfolio.getHistory();
        List<Trade> historicalTrades = new ArrayList<Trade>();
        for (ObjectId tradeId : historicalIds){
            Optional<Trade> getATrade = tradeService.getTradeById(tradeId);
            Trade aTrade = getATrade.get(); // can get null?
            historicalTrades.add(aTrade);
        }
        return historicalTrades;
    }

    public List<Trade> getPendingTrades(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);
        Portfolio portfolio = retrivePortfolio.get(); // can get null
        // want to add null portfolio exception handling?

        // get pending trades
        List<ObjectId> pendingIds = portfolio.getHistory();
        List<Trade> pendingTrades = new ArrayList<Trade>();
        for (ObjectId tradeId : pendingIds){
            Optional<Trade> getATrade = tradeService.getTradeById(tradeId);
            Trade aTrade = getATrade.get(); // can get null?
            pendingTrades.add(aTrade);
        }
        return pendingTrades;
    }


    public void addPortfolio(ObjectId userId, Portfolio port){
        //find user by id 
        User curUser = userService.getUser(userId);
        curUser.addToPortfolio(port.getId());
        //update user by adding current portfolio to user 
        userService.updateUser(curUser);
        repo.save(port);        
    }

    /**
     * FOR TESTING PURPOSES. WANT TO MANUALLY CREATE A PORTFOLIO
     */
    public Portfolio addPortfolio(){
        Portfolio newPortfolio = new Portfolio();
        return repo.insert(newPortfolio);
    }

    // Called when TradeService calls add Trade
    public void addTrade(ObjectId tradeId, ObjectId portfolioId){
        Optional<Portfolio> retrievePortfolio = repo.findById(portfolioId);
        if (!retrievePortfolio.isPresent()){
			log.log(Level.WARNING, "This portfolio does not exist in repo");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} else {
            Portfolio portfolio = retrievePortfolio.get();
            log.log(Level.INFO, "Portfolio retrieved with id: " + portfolio.getId());
            log.log(Level.INFO, "Trying to add trade id: " + tradeId);
            portfolio.addTradeIdToOutstanding(tradeId);
            portfolio.addTradeIdToHistory(tradeId);
            repo.save(portfolio);
        }
    }

    public void addStock(String ticker, int quantity, ObjectId portfolioId){
        try{
            Portfolio portfolio = getPortfolio(portfolioId);
            HashMap<String, Stock> portfolio_stocks = portfolio.getStocks();
            if (portfolio_stocks.containsKey(ticker) == false){ //Stock does not exist in portfolio yet
                Stock new_stock = new Stock();
                new_stock.setAmount(quantity);
                new_stock.setTicker(ticker);
                portfolio_stocks.put(ticker, new_stock);
            } else {
                Stock stock = portfolio_stocks.get(ticker);
                stock.setAmount(stock.getAmount() + quantity);
            }
            repo.save(portfolio);
        }
        catch(ResponseStatusException ex){
            log.log(Level.WARNING, "This portfolio does not exist in repo");
        }
    }

    public void removeStock(String ticker, int quantity, ObjectId portfolioId){
        try{
            Portfolio portfolio = getPortfolio(portfolioId);
            HashMap<String, Stock> portfolio_stocks = portfolio.getStocks();
            if (portfolio_stocks.containsKey(ticker) == false){
                log.log(Level.WARNING, "This ticker does not exist in portfolio");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            Stock stock = portfolio_stocks.get(ticker);
            if (stock.getAmount() < quantity){
                log.log(Level.WARNING, "Insufficient stocks to remove");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                stock.setAmount(stock.getAmount() - quantity);
                repo.save(portfolio);
            }
        }
        catch(ResponseStatusException ex){
            log.log(Level.WARNING, "This portfolio does not exist in repo");
        }
        
    }
    
    public Portfolio getPortfolio(ObjectId portfolioId){
        Optional<Portfolio> retrievePortfolio = repo.findById(portfolioId);
        if (!retrievePortfolio.isPresent()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} 
        return retrievePortfolio.get();
    }

    
    @Scheduled(fixedDelay = 1000)
    public void scheduleUpdateOutstandingTrade() {
        log.info("start the interval call to update the outsanding trade for all the portfolios in dbs");
        Trade curTrade;
        List<Portfolio> portList = repo.findAll();
        double expense;
        //update the outstandinList for each portfolio we have
        for(Portfolio p: portList){
            for(ObjectId id:p.getOutstandingList()){
                //check the status of outstanding list 
                curTrade = tradeService.getTradeById(id).orElse(null);
                //if current trade is not null and the current trade has been fulfilled
                if(curTrade != null & curTrade.gettStatus()==TradeStatus.FILLED){
                    //remove the trade from the outStandinglist
                    p.removeTradeIdFromOutstanding(id);
                    
                    //set the money change
                    expense = curTrade.getQuantity()*curTrade.getRequestPrice();
                    p.setCashOnHand(p.getCashOnHand()+expense);
                    p.setTotalExpense(p.getTotalExpense()+expense);
                    
                    //save to repo
                    repo.save(p);
                   
                }

                
            }
        }
        log.info("end of scheduled job");

        

    }

}