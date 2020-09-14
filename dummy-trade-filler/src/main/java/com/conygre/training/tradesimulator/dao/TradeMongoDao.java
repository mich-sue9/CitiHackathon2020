package com.conygre.training.tradesimulator.dao;

import java.util.List;

import com.conygre.training.tradesimulator.model.Trade;
import com.conygre.training.tradesimulator.model.Trade.TradeStatus;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeMongoDao extends MongoRepository<Trade, ObjectId> {

	public List<Trade> findBytStatus(TradeStatus state);
}