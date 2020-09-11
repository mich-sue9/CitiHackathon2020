package com.congyre.trade.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
    @Id
	private ObjectId UserId;
	private String UserName;
	private List<ObjectId> portfolioList;

    public ObjectId getUserId() {
        return UserId;
    }

    public void setUserId(ObjectId userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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
