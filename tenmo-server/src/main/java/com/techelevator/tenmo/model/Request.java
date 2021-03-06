package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Request {

	private Long transferId;
	private String transferType;
	private String transferStatus;
	private Long accountFrom;
	private Long accountTo;
	private BigDecimal amount;
	
	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public Long getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(Long accountFrom) {
		this.accountFrom = accountFrom;
	}

	public Long getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(Long accountTo) {
		this.accountTo = accountTo;
	}



}
