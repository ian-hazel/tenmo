package com.techelevator.tenmo.models;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Account {

	private BigDecimal balance;
	private Long accountId;
	private Long userId;
	
	public Account() {
	}
	
	public Account(Long accountId, Long userId, BigDecimal balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String balanceToString() {
		return NumberFormat.getCurrencyInstance().format(balance);
	}
	
}
