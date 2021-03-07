package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

public interface RequestDAO {

	List<Request> getAllRequests(String username);

	Request getRequestByTransferId(Long transferId, String username) throws TransferNotFoundException;

	void approveRequest(Long transferId, String username) throws TransferNotFoundException;

	void rejectRequest(Long transferId) throws TransferNotFoundException;

}
