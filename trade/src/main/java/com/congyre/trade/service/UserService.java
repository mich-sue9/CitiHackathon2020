package com.congyre.trade.service;

import java.util.ArrayList;
import java.util.List;

import com.congyre.trade.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;


public class UserService {
    @Autowired
    private UserRepository repo;

    public List<User> getAllClients(){
		List<User> users = new ArrayList<>();
		repo.findAll().forEach(users::add);
		return users;
	}
	
	public User getUser(String userId){
		return repo.findById(userId).orElse(null);
	}
	
	public void addUser(User user){
		repo.save(user); 
	}
	
	public void updateUser(User user){
		repo.save(user);
	}
	
	public void deleteUser(ObjectId userId){
		repo.deleteById(userId);
	}
    
}
