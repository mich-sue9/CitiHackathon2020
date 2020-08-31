package com.congyre.trade.repository;

import java.util.List;

import com.congyre.trade.entity.Trade;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TradeRepository extends MongoRepository<Trade, ObjectId> {
    @Query("{'stockTicker': ?0}")
	public List<Trade> customFindByStockTicker(String stockTicker);
    
}