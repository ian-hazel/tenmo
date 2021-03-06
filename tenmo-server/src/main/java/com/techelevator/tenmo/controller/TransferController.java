package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

	private TransferDAO transferDao;
	
	public TransferController(TransferDAO transferDao) {
		this.transferDao = transferDao;
	}
	
	@RequestMapping(value = "/transfers", method = RequestMethod.GET)
	public List<Transfer> getTransfers(Principal principal) {
		return transferDao.getTransferHistory(principal);
	}
	
}
