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

	/**
	 * This function is mapped to GET request of the url/api/trades
	 * This function retrive all the trade entities from the DB by calling the service 
	 * @return the list of trade entity in the DB
	 */

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Trade> getAllTrades() {
		return service.getAllTrade();
	}

	/**
	 * getTreadeById is mapped to the get request of the url /api/trades/{id}
	 * @param id this is the id of trade that is passed from the url and used to retrieve the trade
	 * entity in the DB	
	 * @return if the Trade with the given id found in the DB then return the Trade entity 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Optional<Trade> getTradeById(@PathVariable("id") String id) {
		checkTradeIdExists(id);
		return service.getTradeById(new ObjectId(id));
    }
	
	/**
	 * getTradesByTiackers is mapped to the get request from the url /api/trades/ticker/{tickerName} and use the ticker 
	 * provided in the url to find the list of trade that contains that ticker
	 * @param tickerName is path varible from the url, the tickerName holds the ticker of a stock 
	 * this is used to for retrieve stock in the DB
	 * @return return a list of trade entities that have the ticker passed from the url
	 */
    @RequestMapping(method = RequestMethod.GET, value = "/ticker/{tickerName}")
	public List<Trade> getTradesByTicker(@PathVariable("tickerName") String tickerName) {
        return service.getTradesByTicker(tickerName);
    }

	/**
	 * addTrade is mapped to POST request from the url /api/trades/addTrade/{protfolioId}, this function takes in the 
	 * Trade entity from the request body and save it to our tradeDB, as well calls the portfolio service 
	 * update the history list and outstanding list by adding the trade id of this trade to theses two lists
	 * @param newTrade a trade entity passed from the request body
	 * @param id the id of the portfolio that is need to updated (uppdating it's history and outstanding list)
	 */
	@RequestMapping(method = RequestMethod.POST, value="/addTrade/{portfolioId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void addTrade(@RequestBody Trade newTrade, @PathVariable("portfolioId") String id) {
		service.addTrade(newTrade, new ObjectId(id));
	}
	
	
	/**
	 * deleteTradeById is a delete request map to /api/trades/{id} which takes the id from the path and delete
	 * the trade entity with that id in the DB
	 * @param id this is the id of the trade that need to be delete
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteTradeById(@PathVariable("id") String id) {
		checkTradeIdExists(id);
		service.deleteTradeById(new ObjectId(""+id));
	}

	/**
	 * cancelTrade is the put request map to /api/trade/cancel.
	 * this function is used udpate the trade entity's tradeStatus to cancel.
	 * a id is take from the request param as /api/trade/cancel?={id}
	 * @param id the id of the trade that need to be changed
	 */
	@RequestMapping(method = RequestMethod.PUT, value="/cancel")
	public void cancelTrade(@RequestParam String id) {
		checkTradeIdExists(id);
		service.cancelTrade(new ObjectId(id));
	}

	/**
	 * checkTradeIDExists is a helpper function to check where the id given is a valid id
	 * in the tradeDB. if the id is not valid they an expection will be throw and if the id is not
	 * found from the DB then an expection will also be throw
	 * @param id the id of the trade that is need to be checked
	 */
	public void checkTradeIdExists(String id){
		if (!ObjectId.isValid(id)){
			logger.log(Level.WARNING, "This is not a valid ID ");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} 
		Optional<Trade> trade = service.getTradeById(new ObjectId(id));
		if (!trade.isPresent()){
			logger.log(Level.WARNING, "This trade does not exist");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	

}
