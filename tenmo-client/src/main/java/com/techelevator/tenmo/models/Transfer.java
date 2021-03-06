package com.techelevator.tenmo.models;

import java.math.BigDecimal;
import java.security.Principal;

public class Transfer {

	private Long transferId;
	private Long userFromId;
	private Long userToId;
	private BigDecimal amount;
	private Principal principal;
	
	public enum Type { REQUEST, SEND }
	private Type type;
	
	public enum Status { PENDING, APPROVED, REJECTED }
	private Status status;
	
	public Transfer() {
		
	}
	/*
	public Transfer(BigDecimal amount, Type type, Status status) {
		this.amount = amount;
		this.type = type;
		this.status = status;
	}
	*/
	
	public Transfer(Long transferId, Long userFromId, Long userToId, BigDecimal amount, Type type, Status status) {
		this.transferId = transferId;
		this.userFromId = userFromId;
		this.userToId = userToId;
		this.amount = amount;
		this.type = type;
		this.status = status;	
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public Long getUserFromId() {
		return userFromId;
	}

	public void setUserFromId(Long userFromId) {
		this.userFromId = userFromId;
	}

	public Long getUserToId() {
		return userToId;
	}

	public void setUserToId(Long userToId) {
		this.userToId = userToId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
	
}
