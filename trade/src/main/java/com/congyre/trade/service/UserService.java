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

	/**
	 * This method gets all the us entity in database
	 * @return List of User Object from the database
	 */
    public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		repo.findAll().forEach(users::add);
		return users;
	}
	
	/**
	 * 
	 * @param userId ObjectId of a user entity
	 * @return the User entity with the userId from the database
	 */
	public User getUser(ObjectId userId){
		return repo.findById(userId).orElse(null);
	}
	

	/**
	 * 
	 * @param user a User entity that is needed to update in the database
	 * @return the updated user in the database
	 */
	public User updateUser(User user){
		//if user does not have a list of portfolio , create one
		if(user.getPortfolioList() == null)
			user.setPortfolioList(new ArrayList<>());
		return repo.save(user);
	}

	/**
	 * 
	 * @param userId the ObejctId of a user that is needed to be deleted
	 */
	
	public void deleteUser(ObjectId userId){
		repo.deleteById(userId);
	}
    
}
