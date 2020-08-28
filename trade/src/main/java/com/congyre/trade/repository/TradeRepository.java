package com.congyre.trade.repository;

import com.congyre.trade.entity.Trade;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeRepository extends MongoRepository<Trade, ObjectId> {
    
    
}