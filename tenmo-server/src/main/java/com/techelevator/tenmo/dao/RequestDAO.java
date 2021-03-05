package com.techelevator.tenmo.dao;

import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Request;

public interface RequestDAO {

	List<Request> getAll(Principal principal);
	
}
