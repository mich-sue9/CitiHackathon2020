package com.congyre.trade.repository;

import java.util.List;
import java.util.Optional;

import com.congyre.trade.entity.Portfolio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioRepository extends MongoRepository<Portfolio, ObjectId> {
    public Optional<Portfolio> findByUserId(ObjectId userId);
    
}
