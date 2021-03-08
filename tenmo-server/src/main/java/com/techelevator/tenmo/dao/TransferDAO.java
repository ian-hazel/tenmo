package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

public interface TransferDAO {

	List<Transfer> getTransferHistory(Principal principal);

	Transfer getTransferDetails(Long transferId, Principal principal) throws TransferNotFoundException;

    // TODO: BUG: should not return void
	// TODO: takes in Transfer
	void sendMoney(BigDecimal amount, Long accountToId, Principal principal) throws AccountNotFoundException;

    // TODO: BUG: should not return void
	void requestMoney(BigDecimal amount, Long accountToId, Principal principal) throws AccountNotFoundException;

}
