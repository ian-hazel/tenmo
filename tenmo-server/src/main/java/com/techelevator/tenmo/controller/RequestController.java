package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.RequestDAO;
import com.techelevator.tenmo.model.Request;

@RestController
@PreAuthorize("isAuthenticated()")
public class RequestController {

	private RequestDAO requestDao;

	public RequestController(RequestDAO requestDao) {
		this.requestDao = requestDao;
	}

	@RequestMapping(value = "/requests", method = RequestMethod.GET)
	public List<Request> getBalance(Principal principal) {
		return requestDao.getAll(principal);
	}
}
