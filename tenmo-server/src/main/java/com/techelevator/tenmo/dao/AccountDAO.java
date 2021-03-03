package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	double getBalanceById(Long accountId);
	
	void setBalanceById(Long accountId, double balance);
		
}
