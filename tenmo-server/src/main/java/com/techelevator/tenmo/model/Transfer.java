package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

	private String type;
	private String status;
	private BigDecimal amount;
	private Long transferId;
	private Long accountFromId;
	private Long accountToId;

	public String getType() {
		if(type.equals("1")) {
			return "Request";
		} else {
			return "Send";
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		if(status.equals("1")) {
			return "Pending";
		} else if(status.equals("2")) {
			return "Approved";
		} else {
			return "Rejected";
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public Long getAccountFromId() {
		return accountFromId;
	}

	public void setAccountFromId(Long accountFromId) {
		this.accountFromId = accountFromId;
	}

	public Long getAccountToId() {
		return accountToId;
	}

	public void setAccountToId(Long accountToId) {
		this.accountToId = accountToId;
	}

}
