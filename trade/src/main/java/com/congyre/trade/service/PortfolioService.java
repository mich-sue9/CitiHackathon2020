package com.congyre.trade.service;

import com.congyre.trade.entity.Portfolio;
import com.congyre.trade.entity.Stock;
import com.congyre.trade.entity.Trade;
import com.congyre.trade.entity.User;
import com.congyre.trade.entity.Trade.TradeStatus;
import com.congyre.trade.repository.PortfolioRepository;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.server.ResponseStatusException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class PortfolioService {
    private static Logger log = Logger.getLogger(PortfolioService.class.getName());

    @Autowired
    private PortfolioRepository repo;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    /***
     * Retrieve live prices for the tickers in portfolio with portId.
     * 
     * @param portId Portfolio id to retrieve live prices for
     * @return JSONObject Contains stock ticker, their price, and their quantity.
     */
    public JSONObject getTickerPrices(ObjectId portId) {
        HashMap<String, Stock> tickerStockMapping = getStocks(portId);
        JSONObject returnObj = new JSONObject();
        double totalValuation = 0;
        JSONArray stockList = new JSONArray();
        for (String ticker : tickerStockMapping.keySet()) {
            try {
                HttpResponse<JsonNode> response = Unirest.get(
                        "https://pxqzjt6ami.execute-api.us-east-1.amazonaws.com/default/filePriceFeed?ticker={tickerName}&num_days=1")
                        .routeParam("tickerName", ticker).asJson();
                JSONObject completeObj = response.getBody().getObject();
                log.log(Level.INFO, completeObj.toString());
                JSONArray arrayPrice = completeObj.getJSONArray("price_data");
                JSONArray prices = arrayPrice.getJSONArray(0);
                Double stockPrice = prices.getDouble(1);
                int quantity = tickerStockMapping.get(ticker).getAmount();

                JSONObject tickerInfo = new JSONObject();
                tickerInfo.put("ticker", ticker);
                tickerInfo.put("price", stockPrice);
                tickerInfo.put("quantity", tickerStockMapping.get(ticker).getAmount());
                totalValuation += quantity * stockPrice;
                stockList.put(tickerInfo);

                log.log(Level.INFO, "Ticker: " + ticker + " price: " + stockPrice);
                
            } catch (Exception ex) {
                log.log(Level.WARNING, "Ticker is not covered by the Live API:" + ticker);

            }
        }
        returnObj.put("valuation", totalValuation);
        returnObj.put("stockPrice", stockList);
        return returnObj;
    }

    /***
     * Retrieves portfolio with id from the repository
     * 
     * @param id Portfolio id to retrieve
     * @return Portfolio object with that id if it exists.
     */
    public Portfolio getportfolio(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id); // can be empty
        if (retrivePortfolio.isPresent()) {
            log.log(Level.INFO, "Portfolio retrieved with id: " + id);
            return retrivePortfolio.get();
        } else {
            log.log(Level.WARNING, "This portfolio with id: " + id + "does not exist in repo");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Retrieve all trades made by this portfolio id
     * 
     * @param id Portfolio id to retrieve trade history of
     * @return List<Trade> containing all Trades made by this portfolio
     */
    public List<Trade> getTradeHistory(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);// can be empty
        if (retrivePortfolio.isPresent()) {
            log.log(Level.INFO, "Portfolio retrieved with id: " + id);
            log.log(Level.INFO, "Trying to get gistorical trades of the portfolio with id: " + id);

            Portfolio portfolio = retrivePortfolio.get();

            // get historical trades
            List<ObjectId> historicalIds = portfolio.getHistory();
            List<Trade> historicalTrades = new ArrayList<Trade>();

            for (ObjectId tradeId : historicalIds) {
                Optional<Trade> getATrade = tradeService.getTradeById(tradeId); // can be empty

                // add the trade into the return list, if we can find it successfully
                if (getATrade.isPresent()) {
                    Trade aTrade = getATrade.get();
                    historicalTrades.add(aTrade);
                } else {
                    log.log(Level.WARNING, "Cannot find the stock with id: " + tradeId
                            + " in the trade history of the portfolio: " + id);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    // trade not found error happens when the addTrade function is not working
                    // properly
                }
            }
            // want the result dispalyed from the latest to the oldest
            Collections.reverse(historicalTrades);
            return historicalTrades;

        } else {
            log.log(Level.WARNING, "This portfolio with id: " + id + "does not exist in repo");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Retrieve all Trades that are currently still awaiting processing by the dummy
     * trade fufiller.
     * 
     * @param id Portfolio id to retrieve pending trades for
     * @return List<Trade> contains Trades are are still not processed yet.
     */
    public List<Trade> getPendingTrades(ObjectId id) {
        Optional<Portfolio> retrivePortfolio = repo.findById(id);
        if (retrivePortfolio.isPresent()) {
            log.log(Level.INFO, "Portfolio retrieved with id: " + id);
            log.log(Level.INFO, "Trying to get pending trades of the portfolio with id: " + id);

            Portfolio portfolio = retrivePortfolio.get();

            // get historical trades
            List<ObjectId> pendingIds = portfolio.getOutstandingList();
            List<Trade> pendingTrades = new ArrayList<Trade>();

            for (ObjectId tradeId : pendingIds) {
                Optional<Trade> getATrade = tradeService.getTradeById(tradeId);// can be empty

                // add the trade into the return list, if we can find it succesfully
                if (getATrade.isPresent()) {
                    Trade aTrade = getATrade.get();
                    pendingTrades.add(aTrade);
                } else {
                    log.log(Level.WARNING, "Cannot find the stock with id: " + tradeId
                            + " in the trade history of the portfolio: " + id);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    // trade not found error happens when the addTrade function is not working
                    // properly
                }
            }
            // want the result dispalyed from the latest to the oldest
            Collections.reverse(pendingTrades);
            return pendingTrades;

        } else {
            log.log(Level.WARNING, "This portfolio with id: " + id + "does not exist in repo");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Retrieve ticker and its associated Stock object from a portfolio
     * 
     * @param portId Portfolio ID to get information from
     * @return HashMap<String, Stock> Mapping of the ticker name to its Stock object
     */
    public HashMap<String, Stock> getStocks(ObjectId portId) {
        try {
            Portfolio portfolio = getPortfolioRepo(portId);
            HashMap<String, Stock> portfolio_stocks = portfolio.getStocks();
            return portfolio_stocks;
        } catch (ResponseStatusException ex) {
            log.log(Level.WARNING, "This portfolio does not exist in repo");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Add a portfolio object into the repository and set the user it belongs to.
     * 
     * @param userId    User ID of the user who owns the portfolio
     * @param portfolio Portfolio that will be added to repo.
     */
    public void addPortfolio(ObjectId userId, Portfolio portfolio) {
        // find user by id
        User curUser = userService.getUser(userId);
        if (curUser == null)
            curUser = userService.updateUser(new User());

        // set userId of port
        portfolio.setUserId(userId);
        // create an id for the port
        ObjectId pId = repo.insert(portfolio).getId();
        // add id to user
        curUser.addToPortfolio(pId);
        // update user by adding current portfolio to user
        userService.updateUser(curUser);
    }

    /**
     * Adds a new portfolio into the repository
     * 
     * @return Portfolio object that was added into the repo
     */
    public Portfolio addPortfolio() {
        Portfolio newPortfolio = new Portfolio();
        return repo.insert(newPortfolio);
    }

    /***
     * Adds a trade to a Portfolio's outstanding and history list. Used when
     * TradeService calls add Trade. Needed to keep track of fufilment updates of a
     * trade.
     * 
     * @param tradeId     Id of Trade to add to the portfolio's outstanding and
     *                    history list
     * @param portfolioId Id of Portfolio to add the Trade to.
     */
    public void addTrade(ObjectId tradeId, ObjectId portfolioId) {
        Optional<Portfolio> retrievePortfolio = repo.findById(portfolioId);
        if (!retrievePortfolio.isPresent()) {
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

    /***
     * Creates a stock with the given ticker and quantity. Adds this stock to a
     * portfolio
     * 
     * @param ticker      Name of the stock ticker
     * @param quantity    The number of stock to add of this ticker
     * @param portfolioId The ID of the Portfolio that will receive the new addition
     *                    of stocks
     */
    public void addStock(String ticker, int quantity, ObjectId portfolioId) {
        try {
            Portfolio portfolio = getPortfolioRepo(portfolioId);
            HashMap<String, Stock> portfolio_stocks = portfolio.getStocks();
            if (portfolio_stocks.containsKey(ticker) == false) { // Stock does not exist in portfolio yet
                Stock new_stock = new Stock();
                new_stock.setAmount(quantity);
                new_stock.setTicker(ticker);
                portfolio_stocks.put(ticker, new_stock);
            } else {
                Stock stock = portfolio_stocks.get(ticker);
                stock.setAmount(stock.getAmount() + quantity);
            }
            repo.save(portfolio);
        } catch (ResponseStatusException ex) {
            log.log(Level.WARNING, "This portfolio does not exist in repo");
        }
    }

    /***
     * Removes a Stock from the Portfolio once the quantity reaches 0
     * 
     * @param ticker      The ticker to remove stocks from
     * @param quantity    The number of stocks to remove
     * @param portfolioId The id of the portfolio to remove stocks from
     */
    public void removeStock(String ticker, int quantity, ObjectId portfolioId) {
        try {
            Portfolio portfolio = getPortfolioRepo(portfolioId);
            HashMap<String, Stock> portfolio_stocks = portfolio.getStocks();
            if (portfolio_stocks.containsKey(ticker) == false) {
                log.log(Level.WARNING, "This ticker does not exist in portfolio");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            Stock stock = portfolio_stocks.get(ticker);
            if (stock.getAmount() < quantity) {
                log.log(Level.WARNING, "Insufficient stocks to remove");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (stock.getAmount() == quantity) {
                portfolio_stocks.remove(ticker);
                repo.save(portfolio);
            } else {
                stock.setAmount(stock.getAmount() - quantity);
                repo.save(portfolio);
            }
        } catch (ResponseStatusException ex) {
            log.log(Level.WARNING, "This portfolio does not exist in repo");
        }

    }

    /***
     * Retrieve a Portfolio from the repository given a portfolio id
     * 
     * @param portfolioId The porfolio id to retrieve
     * @return The associated Portfolio object
     */
    public Portfolio getPortfolioRepo(ObjectId portfolioId) {
        Optional<Portfolio> retrievePortfolio = repo.findById(portfolioId);
        if (!retrievePortfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return retrievePortfolio.get();
    }

    /***
     * Used when dummy trade service processes a trade It will remove a Trade object
     * from the Portfolio's outstanding list
     * 
     * @param removeList  List of the trade's object ids to remove
     * @param portfolioId The id of the portfolio to remove trades from
     */
    public void removeTradesFromOutstandingList(List<ObjectId> removeList, ObjectId portfolioId) {
        Portfolio p = getPortfolioRepo(portfolioId);
        p.removeTradesFromOutstanding(removeList);
        repo.save(p);
    }

    /**
     * A scheduler that routinely checks the trade status of all trades in all the
     * portfolios in the repository It will update the Stock list accordingly based
     * on whether the trade was accepted or rejected.
     */
    @Scheduled(fixedRate = 10000)
    public void scheduleUpdateOutstandingTrade() {
        log.info("start the interval call to update the outsanding trade for all the portfolios in dbs");
        Trade curTrade;
        List<Portfolio> portList = repo.findAll();
        double expense;
        // update the outstandinList for each portfolio we have
        for (Portfolio p : portList) {
            // create a local varible to hold the list
            List<ObjectId> outListtoRemove = new ArrayList<>();
            for (ObjectId id : p.getOutstandingList()) {
                // check the status of outstanding list
                curTrade = tradeService.getTradeById(id).orElse(null);

                // check if the outstanding is rejected, if yes remove from outstanding list
                if (curTrade != null && curTrade.gettStatus() == TradeStatus.REJECTED) {
                    outListtoRemove.add(id);
                }

                // if current trade is not null and the current trade has been fulfilled
                else if (curTrade != null && curTrade.gettStatus() == TradeStatus.FILLED) {
                    // remove the trade from the outStandinglist
                    outListtoRemove.add(id);

                    // set the money change
                    expense = curTrade.getQuantity() * curTrade.getRequestPrice();
                    p.setCashOnHand(p.getCashOnHand() - expense);
                    p.setTotalExpense(p.getTotalExpense() + expense);

                    // save to repo
                    repo.save(p);

                    // add stock or remove stock according to the trade
                    if (curTrade.getQuantity() > 0)
                        addStock(curTrade.getStockTicker(), curTrade.getQuantity(), p.getId());
                    else
                        removeStock(curTrade.getStockTicker(), Math.abs(curTrade.getQuantity()), p.getId());

                }

            }

            removeTradesFromOutstandingList(outListtoRemove, p.getId());

        }
        log.info("end of scheduled job");
    }
}