package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

	private TransferDAO transferDao;
	private UserDAO userDao;
	
	public TransferController(TransferDAO transferDao, UserDAO userDao) {
		this.transferDao = transferDao;
		this.userDao = userDao;
	}
	
	@RequestMapping(value = "transfers/send", method = RequestMethod.GET)
	public List<User> findForSend() {
		return userDao.findAll();
	}
	
	@RequestMapping(value = "transfers/request", method = RequestMethod.GET)
	public List<User> findForRequest() {
		return userDao.findAll();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "transfers/send", method = RequestMethod.POST)
	public void sendTransfer(BigDecimal amount, Long userToId, Principal principal) 
			throws AccountNotFoundException {
		try {
			transferDao.sendMoney(amount, userToId, principal);
		} catch (AccountNotFoundException e) {
		}
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "transfers/request", method = RequestMethod.POST)
	public void requestTransfer(Principal principal) 
			throws AccountNotFoundException {
		try {
			transferDao.getTransferHistory(principal);
		} catch (AccountNotFoundException e) {
		}
	}
		
	@RequestMapping(value = "transfers/details", method = RequestMethod.GET)
	public List<Transfer> getTransferHistory(Principal principal) {
		return transferDao.getTransferHistory(principal);
	}
	
	@RequestMapping(value = "transfers/details/{id}", method = RequestMethod.GET)
	public Transfer getTransferDetails(@PathVariable Long transferId, Principal principal) 
			throws TransferNotFoundException {
		Transfer transfer = new Transfer();
		try {
			transfer = transferDao.getTransferDetails(transferId, principal);
		} catch (TransferNotFoundException e) {
		}
		return transfer;
	}
	
		
}
