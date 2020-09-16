package com.congyre.trade.rest;

import java.util.List;
import java.util.logging.Logger;
import java.util.HashMap;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.service.PortfolioService;


import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin // allows requests from all domains
public class PortfolioController {
    private static Logger logger = Logger.getLogger(TradeController.class.getName());

    @Autowired
    private PortfolioService service;

    /** Used to get table of stocks and their current prices */
    @RequestMapping(method = RequestMethod.GET, value = "/getStockLivePrice/{portId}")
    public String getStockLivePrice(@PathVariable("portId") String portId) {
        // want to display trade details of all trades under this portfolio
        if ( !ObjectId.isValid(portId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else{
            
            return service.getTickerPrices(new ObjectId(portId)).toString();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Portfolio getAllInPortfolio(@PathVariable("id") String id){
        if ( !ObjectId.isValid(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else{
            try {
                return service.getportfolio(new ObjectId(id));
            } catch (Exception e) {
                throw e;
            }
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/history/{id}")
    public List<Trade> getTradeHistory(@PathVariable("id") String id) {
        // want to display trade details of all trades under this portfolio
        if ( !ObjectId.isValid(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else{
            try {
                return service.getTradeHistory(new ObjectId(id));
            } catch (Exception e) {
                throw e;
            }
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/pending/{id}")
    public List<Trade> getPendingTrades(@PathVariable("id") String id){
        // want to display trade details of pending trades
        if ( !ObjectId.isValid(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else{
            try {
                return service.getPendingTrades(new ObjectId(id));
            } catch (Exception e) {
                throw e;
            }
        }
    }
    

    @RequestMapping(method = RequestMethod.POST, value="/addStock/{portfolioId}/{ticker}/{quantity}")
	public void addStock(@PathVariable("ticker") String ticker, @PathVariable("portfolioId") String portfolioId, @PathVariable("quantity") int quantity) {
        service.addStock(ticker, quantity, new ObjectId(portfolioId));
    }

    @RequestMapping(method = RequestMethod.POST, value="/removeStock/{portfolioId}/{ticker}/{quantity}")
	public void removeStock(@PathVariable("ticker") String ticker, @PathVariable("portfolioId") String portfolioId, @PathVariable("quantity") int quantity) {
        service.removeStock(ticker, quantity, new ObjectId(portfolioId));
    }

  
    @RequestMapping(method = RequestMethod.POST, value="/addPortfolio/{userId}")
    public void addPortfolio(@PathVariable String userId, @RequestBody Portfolio port){
        service.addPortfolio(new ObjectId(userId), port);
    }
      /*** FOR TESTING PURPOSES */
	// public void addPortfolio() {
	// 	service.addPortfolio();
	// }
}


