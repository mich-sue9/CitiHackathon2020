package com.congyre.trade.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Portfolio {

    @Id
    private ObjectId id;

    private ObjectId userId;
    private double totalExpense;

    private double cashOnHand;
    private double totalIncome;
    private HashMap<String, Stock> stocks;
    private List<ObjectId> history;
    private List<ObjectId> outstandingList;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public ObjectId getUserId() {

        return userId;
    }

    public List<ObjectId> getOutstandingList() {
        return outstandingList;
    }

    public void removeTradeIdFromOutstanding(ObjectId tradeId){
        this.outstandingList.remove(tradeId);
    }

    public void addTradeIdToOutstanding(ObjectId tradeId){
        this.outstandingList.add(tradeId);
    }

    public List<ObjectId> getHistory() {
        return history;
    }

    public void addTradeIdToHistory(ObjectId tradeId){
        this.history.add(tradeId);
    }

    public double getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(double cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public HashMap<String, Stock> getStocks() {
        return stocks;
    }

    public void setStocks(HashMap<String, Stock> stocks) {
        this.stocks = stocks;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }



    
}
