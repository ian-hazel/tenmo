package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private Long transferId;
	private String fromName;
	private String toName;
	private Long userId;	
	private BigDecimal amount;
	private String type;
	private String status;
	
	public Transfer() {
		
	}

	public Transfer(Long transferId, String fromName, String toName, BigDecimal amount, String type, String status) {
		this.transferId = transferId;
		this.fromName = fromName;
		this.toName = toName;
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

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
