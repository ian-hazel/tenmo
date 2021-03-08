package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

	private AccountDAO accountDao;

	public AccountController(AccountDAO accountDao) {
		this.accountDao = accountDao;
	}

    // TODO: BUG: add back "/" and make it work.
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {
		// TODO: update to AuthenticatedUser or pass id or something
		return accountDao.getBalance(getAcctIdFromUserName(principal.getName()));
	}

}
