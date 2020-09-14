package com.conygre.training.tradesimulator.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Trade {
    public enum TradeStatus{CREATED, PENDING,CANCELLED,REJECTED,FILLED,PARTIALLY_FILLED,ERROR, PROCESSING}

    @Id
    private ObjectId id;
    private Date dateCreated;
    private String stockTicker;
    private int quantity;
    private double requestPrice;
    private TradeStatus tStatus;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRequestPrice() {
        return requestPrice;
    }

    public void setRequestPrice(double requestPrice) {
        this.requestPrice = requestPrice;
    }

    public TradeStatus gettStatus() {
        return tStatus;
    }

    public void settStatus(TradeStatus tStatus) {
        this.tStatus = tStatus;
    }

    
}