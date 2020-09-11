package com.congyre.trade.repository;

import com.congyre.trade.entity.Portfolio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<Portfolio, ObjectId> {
    
}
