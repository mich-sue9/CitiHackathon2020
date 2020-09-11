package com.congyre.trade.entity;

import java.util.HashMap;

public class Portfolio {

    private int id;
    private int userId;
    private double totalExpense;
    private HashMap<String, Stock> stocks;
    private double cashOnHand;
    private double totalIncome;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
