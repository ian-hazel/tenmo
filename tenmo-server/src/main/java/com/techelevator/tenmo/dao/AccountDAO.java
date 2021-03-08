package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
	
	Long getAcctIdFromUserId(Long userId);

	Long getAcctIdFromUserName(String username);

	BigDecimal getBalance(Long accountId);
	
	BigDecimal increaseBalance(Long accountId, BigDecimal amount);
	
	BigDecimal decreaseBalance(Long accountId, BigDecimal amount);

}