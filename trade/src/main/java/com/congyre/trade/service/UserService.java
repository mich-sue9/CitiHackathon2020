package com.congyre.trade.service;

import java.util.ArrayList;
import java.util.List;

import com.congyre.trade.entity.User;

import com.congyre.trade.repository.UserRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		repo.findAll().forEach(users::add);
		return users;
	}
	
	public User getUser(ObjectId userId){
		return repo.findById(userId).orElse(null);
	}
	

	public User updateUser(User user){
		//if user does not have a list of portfolio , create one
		if(user.getPortfolioList() == null)
			user.setPortfolioList(new ArrayList<>());
		return repo.save(user);
	}
	
	public void deleteUser(ObjectId userId){
		repo.deleteById(userId);
	}
    
}
