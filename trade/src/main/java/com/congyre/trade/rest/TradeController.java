package com.congyre.trade.rest;


import java.util.List;
import java.util.Optional;

import com.congyre.trade.entity.Trade;
import com.congyre.trade.service.TradeService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin // allows requests from all domains
public class TradeController {

	// private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TradeService service;

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Trade> getAllTrades() {
		// logger.info("managed to call a Get request for findAll");
		return service.getAllTrade();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Optional<Trade> getTradeById(@PathVariable("id") String id) {
        return service.getTradeById(new ObjectId(id));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/ticker/{tickerName}")
	public List<Trade> getTradesByTicker(@PathVariable("tickerName") String tickerName) {
        return service.getTradesByTicker(tickerName);
    }

    @RequestMapping(method = RequestMethod.POST)
	public void addTrade(@RequestBody Trade newTrade) {
		service.addTrade(newTrade);
	}
    
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteTradeById(@PathVariable("id") String id) {
		service.deleteTradeById(new ObjectId(""+id));
	}

	@RequestMapping(method = RequestMethod.PUT, value="/cancel")
	public void updateTradeStatus(@RequestParam String id) {
		service.cancelTrade(new ObjectId(id));
	}

	

}
