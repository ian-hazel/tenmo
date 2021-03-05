package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private Long transferId;
	private Long acctFromId;
	private Long acctToId;
	private BigDecimal amount;
	
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
	
	public Transfer(Long transferId, Long acctFromId, Long acctToId, BigDecimal amount, Type type, Status status) {
		this.transferId = transferId;
		this.acctFromId = acctFromId;
		this.acctToId = acctToId;
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

	public Long getAcctFromId() {
		return acctFromId;
	}

	public void setAcctFromId(Long acctFromId) {
		this.acctFromId = acctFromId;
	}

	public Long getAcctToId() {
		return acctToId;
	}

	public void setAcctToId(Long acctToId) {
		this.acctToId = acctToId;
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
	
}
