package com.congyre.trade.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
    @Id
	private ObjectId userId;
	private String userName;
	private List<ObjectId> portfolioList;

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ObjectId> getPortfolioList() {
        return portfolioList;
    }

    public void setPortfolioList(List<ObjectId> portfolioList) {
        this.portfolioList = portfolioList;
    }

    public void addToPortfolio(ObjectId portId){
        this.portfolioList.add(portId);
    }

    public void removeFromPortfolio(ObjectId portId){
        this.portfolioList.remove(portId);
    }
}
