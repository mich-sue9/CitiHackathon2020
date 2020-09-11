package com.congyre.trade.rest;

import java.util.Optional;
import java.util.logging.Logger;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.service.PortfolioService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin // allows requests from all domains
public class PortfolioController {
    private static Logger logger = Logger.getLogger(TradeController.class.getName());

    @Autowired
    private PortfolioService service;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Optional<Portfolio> getAllInPortfolio(@PathVariable("id") String id) {
		return service.getportfolio(new ObjectId(id));
    }
    

    
    public void updateTotalExpense(){

    }

    public void addStock(String ticker, int amount){

    }

    public void removeStock(String ticker, int amount){

    }

    public void addTrade(){

    }

    public void getTradeHistory(ObjectId id){

    }

}


