package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.User;
import com.congyre.trade.entity.Trade.TradeStatus;
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

    @Autowired
    private UserService userService;


    public Optional<Portfolio> getportfolio(ObjectId id) {
        return repo.findById(id);
    }

    public List<ObjectId> getTradeHistory(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);
        Portfolio portfolio = retrivePortfolio.get();
        return portfolio.getHistory(); // can return null
    }

    public void addPortfolio(ObjectId userId, Portfolio port){
        //find user by id 
        User curUser = userService.getUser(userId);
        curUser.addToPortfolio(port.getId());
        //update user by adding current portfolio to user 
        userService.updateUser(curUser);
        repo.save(port);        
    }


    public void addTrade(ObjectId tradeId, ObjectId portfolioId){
        Optional<Portfolio> retrievePortfolio = repo.findById(portfolioId);
        Portfolio portfolio = retrievePortfolio.get();
        portfolio.addTradeIdToOutstanding(tradeId);
        portfolio.addTradeIdToHistory(tradeId);
        repo.save(portfolio);
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

                }

                
            }
        }

        

    }

}