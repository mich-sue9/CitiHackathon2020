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
    private int userId;
    private double totalExpense;

    private double cashOnHand;
    private double totalIncome;
    private HashMap<String, Stock> stocks;
    private HashSet<Trade> history;
    private HashSet<Trade> outstandingList;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    
    public int getUserId() {
        return userId;
    }

    public HashSet<Trade> getOutstandingList() {
        return outstandingList;
    }

    public void setOutstandingList(HashSet<Trade> outstandingList) {
        this.outstandingList = outstandingList;
    }

    public HashSet<Trade> getHistory() {
        return history;
    }

    public void setHistory(HashSet<Trade> history) {
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

    public void setUserId(int userId) {
        this.userId = userId;
    }



    
}
