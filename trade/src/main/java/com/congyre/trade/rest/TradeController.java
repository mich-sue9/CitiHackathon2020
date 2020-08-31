package com.congyre.trade.rest;


import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.congyre.trade.entity.Trade;
import com.congyre.trade.service.TradeService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin // allows requests from all domains
public class TradeController {

	private static Logger logger = Logger.getLogger(TradeController.class.getName());

	@Autowired
	private TradeService service;

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Trade> getAllTrades() {
		return service.getAllTrade();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Optional<Trade> getTradeById(@PathVariable("id") String id) {
		checkTradeIdExists(id);
		return service.getTradeById(new ObjectId(id));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/ticker/{tickerName}")
	public List<Trade> getTradesByTicker(@PathVariable("tickerName") String tickerName) {
        return service.getTradesByTicker(tickerName);
    }

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void addTrade(@RequestBody Trade newTrade) {
		service.addTrade(newTrade);
	}
    
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteTradeById(@PathVariable("id") String id) {
		checkTradeIdExists(id);
		service.deleteTradeById(new ObjectId(""+id));
	}

	@RequestMapping(method = RequestMethod.PUT, value="/cancel")
	public void cancelTrade(@RequestParam String id) {
		checkTradeIdExists(id);
		service.cancelTrade(new ObjectId(id));
	}

	public void checkTradeIdExists(String id){
		Optional<Trade> trade = service.getTradeById(new ObjectId(id));
		if (!trade.isPresent()){
			logger.log(Level.WARNING, "This trade does not exist");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	

}
