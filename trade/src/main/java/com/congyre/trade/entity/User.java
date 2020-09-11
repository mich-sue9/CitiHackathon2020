package com.congyre.trade.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
    @Id
	private ObjectId UserId;
	private String UserName;
	private List<ObjectId> portfoiloList;

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

    public List<ObjectId> getPortfoiloList() {
        return portfoiloList;
    }

    public void setPortfoiloList(List<ObjectId> portfoiloList) {
        this.portfoiloList = portfoiloList;
    }
}
