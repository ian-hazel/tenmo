package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;

public class UserController {
	
	private UserDAO userDao;
	
	public UserController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping(value = "/transfers/send", method = RequestMethod.GET)
	public List<User> findAll() {
		return userDao.findAll();
	}
	
}
