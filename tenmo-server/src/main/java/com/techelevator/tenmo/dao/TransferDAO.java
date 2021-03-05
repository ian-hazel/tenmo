package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> getAll(Principal principal);

	Integer sendMoney(BigDecimal amount, Long accountToId, Principal principal);
	
	Integer requestMoney(BigDecimal amount, Long accountToId, Principal principal);
	
	Integer approveTransfer(Long transferId, Principal principal);
	
	Integer rejectTransfer(Long transferId, Principal principal);
}
