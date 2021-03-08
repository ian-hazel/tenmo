// TODO: BUG: REWRITE THIS
package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferSqlDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
@RequestMapping("/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
	
	private TransferDAO transferDao;
	private UserDAO userDao;
	private AccountDAO acctDao;
	
	public TransferController(TransferDAO transferDao, UserDAO userDao, AccountDAO acctDao) {
		this.transferDao = transferDao;
		this.userDao = userDao;
		this.acctDao = acctDao;
	}
	
	
	
	@RequestMapping( path = "/send" , method = RequestMethod.POST)
	public String sendTransfer(@Valid @RequestBody Transfer transfer) {
		// TODO: finish method
		String response = "";
		if (transfer.getUserToId() != transfer.getUserFromId()) {
			//accountDao.decreaseBalance(transfer.getSendingAccount, transfer.getAmount);
			//accountDao.increaseBalance(transfer.getReceivingAccount, transfer.getAmount);
			// int results = transferDao.createTransfer(transfer);
			// if (results == 1) {
			// 		response = "Transfer Approved";
			// }
			// else {
			// 		response = "Transfer Failed";
			// }
			// return response;
		}
		
		return null;
	}
	
	@RequestMapping( path = "/userlist" , method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userDao.findAll();
	}
	
	
	
	
	
	
	
}