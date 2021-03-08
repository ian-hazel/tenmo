// TODO: BUG: REWRITE THIS
package com.techelevator.tenmo.controller;

import java.security.Principal;
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
		if (transfer.getUserToId() == transfer.getUserFromId()) {
			response = "You can't send money to yourself!";
			return response;
		}
		if (acctDao.getBalance(transfer.getUserFromId()).compareTo(transfer.getAmount()) > 0) {
			acctDao.decreaseBalance(transfer.getUserFromId(), transfer.getAmount());
			acctDao.increaseBalance(transfer.getUserToId(), transfer.getAmount());
			int results = transferDao.sendMoney(transfer);
			if (results == 1) {
				response = "Transfer Approved";
			}
			else {
				response = "Transfer Failed";
			}
		}
		else {
			response = "Insufficient Funds";
		}
		return response;
	}
	
	@RequestMapping( path = "/request" , method = RequestMethod.POST)
	public String requestTransfer(@Valid @RequestBody Transfer transfer) {
		// TODO: finish method
		String response = "";
		if (transfer.getUserToId() == transfer.getUserFromId()) {
			response = "You can't request money from yourself!";
			return response;
		}
		int results = transferDao.requestMoney(transfer);
		if (results == 1) {
			response = "Request Sent";
		}
		else {
			response = "Request Failed";
		}
		return response;
	}
	
	@RequestMapping( path = "/userlist" , method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userDao.findAll();
	}
	
	@RequestMapping( path = "" , method = RequestMethod.GET)
	public List<Transfer> getAllTransfers(Principal principal) {
		return transferDao.getTransferHistory(principal);
	}
	
	@RequestMapping ( path = "/{id}" , method = RequestMethod.GET)
	public Transfer getTransferById(@PathVariable long id) {
		return transferDao.getTransferDetails(id);
	}
	
	@RequestMapping( path = "/userlist/{id}" , method = RequestMethod.GET)
	public User getUserById(@PathVariable long id) {
		return userDao.findByUserId(id);
	}
	
	@RequestMapping( path = "/pendingrequests" , method = RequestMethod.GET)
	public List<Transfer> getPendingRequests(Principal principal) {
		return transferDao.getPendingRequests(principal);
	}
	
	@RequestMapping( path = "/pendingrequests/{id}/approve" , method = RequestMethod.PUT)
	public String approveRequest(@Valid @RequestBody Transfer transfer, @PathVariable long id) {
		String response = "";
		int updateCheck = 0;
		if (acctDao.getBalance(transfer.getUserFromId()).compareTo(transfer.getAmount()) > 0) {
			updateCheck = transferDao.approveRequest(transfer);
			if (updateCheck == 1) {
				acctDao.decreaseBalance(transfer.getUserFromId(), transfer.getAmount());
				acctDao.increaseBalance(transfer.getUserToId(), transfer.getAmount());
				response = "Approval successful, transfer complete";
			}
			else {
				response = "Approval failed";
			}
		}
		else {
			response = "Insufficient funds to approve request";
		}
		return response;
	}
	
	@RequestMapping ( path = "/pendingrequests/{id}/reject", method = RequestMethod.PUT)
	public String rejectRequest(@Valid @RequestBody Transfer transfer, @PathVariable long id) {
		String response = "";
		int updateCheck = 0;
		updateCheck = transferDao.rejectRequest(transfer);
		if (updateCheck == 1) {
			response = "Rejection successful";
		}
		else {
			response = "Rejection failed";
		}
		return response;
	}
}