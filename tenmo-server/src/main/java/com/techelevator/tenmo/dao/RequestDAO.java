package com.techelevator.tenmo.dao;

import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

public interface RequestDAO {

	List<Request> getAllRequests(Principal principal);

	Request getRequestByTransferId(Long transferId, Principal principal) throws TransferNotFoundException;

	void approveRequest(Long transferId, Principal principal) throws TransferNotFoundException;

	void rejectRequest(Long transferId, Principal principal) throws TransferNotFoundException;

}
