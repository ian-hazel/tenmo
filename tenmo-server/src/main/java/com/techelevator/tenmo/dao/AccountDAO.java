package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDAO {

	BigDecimal getBalance(Principal principal);
	
	void setBalance(Principal principal, BigDecimal balance);
		
}
