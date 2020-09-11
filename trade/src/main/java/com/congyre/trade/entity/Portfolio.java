package com.congyre.trade.entity;

import java.util.HashMap;
import java.util.HashSet;

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
    private HashSet<ObjectId> history;
    private HashSet<ObjectId> outstandingList;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public ObjectId getUserId() {

        return userId;
    }

    public HashSet<ObjectId> getOutstandingList() {
        return outstandingList;
    }

    public void setOutstandingList(HashSet<ObjectId> outstandingList) {
        this.outstandingList = outstandingList;
    }

    public HashSet<ObjectId> getHistory() {
        return history;
    }

    public void setHistory(HashSet<ObjectId> history) {
        this.history = history;
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
