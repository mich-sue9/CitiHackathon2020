package com.congyre.trade.rest;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.congyre.trade.entity.User;
import com.congyre.trade.service.UserService;

/**
 * UserController handles the api call related to user currently only containing basic CRUD 
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
	

	@Autowired
	private UserService service;


	/**
	 * the function handle the get method for getting all users in the DB
	 * @return List of all the User from the DB
	 */
	@RequestMapping(method = RequestMethod.GET, value="/user")
	public List<User> getAllUsers(){
		return service.getAllUsers();
	}
	
	/**
	 * the function takes in the userId in the path and return the user with that id from the DB
	 * @param userId userId is passed from the path and is used get the specific user from DB
	 * @return the user retrieved from database
	 */
	@RequestMapping("/user/{userId}")
	public User getUser(@PathVariable String userId) {
		return service.getUser(new ObjectId(userId));
	}

	/**
	 * The function takes in a User entity in the request body and update that to the DB
	 * @param user the user entity from the request body
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user")
	public void addUser(@RequestBody User user) {
		service.updateUser(user);
	}
	
	/**
	 * the function takes in the id for a User entity in the DB and delete the corresponding user in the DB
	 * @param userId a Id of the user in the database
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/user/{userId}")
	public void deleteClient(@PathVariable String userId) {
		service.deleteUser(new ObjectId(userId));
	}
}

