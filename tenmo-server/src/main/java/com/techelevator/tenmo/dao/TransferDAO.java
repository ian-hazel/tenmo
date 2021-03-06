package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> getTransferHistory(Principal principal);

	void sendMoney(BigDecimal amount, Long accountToId, Principal principal);
	
	void requestMoney(BigDecimal amount, Long accountToId, Principal principal);
	
	void approveTransfer(Long transferId, Principal principal);
	
	void rejectTransfer(Long transferId, Principal principal);
}
