package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.RequestDAO;
import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

@RestController
@PreAuthorize("isAuthenticated()")
public class RequestController {

	private RequestDAO requestDao;

	public RequestController(RequestDAO requestDao) {
		this.requestDao = requestDao;
	}

	@RequestMapping(value = "/requests", method = RequestMethod.GET)
	public List<Request> getAllRequests(Principal principal) {
		return requestDao.getAllRequests(principal);
	}
	
	@RequestMapping(value = "/requests/{id}", method = RequestMethod.GET)
	public Request viewRequestById(@PathVariable Long transferId, Principal principal) 
			throws TransferNotFoundException {
		return requestDao.getRequestByTransferId(transferId, principal);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/requests/{id}", method = RequestMethod.PUT)
	public void approveRequestById(@PathVariable Long transferId, Principal principal, @RequestParam(required = true) Long append) 
			throws TransferNotFoundException {
		try {
			if(append == 1) {
				requestDao.approveRequest(transferId, principal);
			} else if (append == 2){
				requestDao.rejectRequest(transferId, principal);
			} else {
			}
		} catch(TransferNotFoundException e) {
		}
	}
}
