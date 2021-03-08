package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

public interface TransferDAO {

	List<Transfer> getTransferHistory(Principal principal);

	Transfer getTransferDetails(Long transferId) throws TransferNotFoundException;

	int sendMoney(Transfer transfer) throws AccountNotFoundException;
	
	int requestMoney(Transfer transfer) throws AccountNotFoundException;
	
	List<Transfer> getPendingRequests(Principal principal);
	
	int approveRequest(Transfer transfer) throws TransferNotFoundException;
	
	int rejectRequest(Transfer transfer) throws TransferNotFoundException;

}

// void sendMoney(BigDecimal amount, Long accountToId, Principal principal) throws AccountNotFoundException;
// void requestMoney(BigDecimal amount, Long accountToId, Principal principal) throws AccountNotFoundException;