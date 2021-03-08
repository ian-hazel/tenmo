package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	public TransferService(String url) {
		BASE_URL = url;
	}
	
	/**
	 * Creates a new send transfer to send money 
	 * from one account to another
	 * Type should be automatically set to SEND
	 * Status should be automatically set to APPROVED
	 * 
	 * @param user, amount, receivingAcct
	 * @return new sent transfer
	 */
	public String sendTransfer(BigDecimal amount, Long userToId, AuthenticatedUser user) {
		
		Transfer toSend = makeBasicTransfer(amount, userToId, user);
		toSend.setType(Transfer.Type.SEND);
		toSend.setStatus(Transfer.Status.APPROVED);
		
		String url = BASE_URL + "/transfers/send";
		String status = null;
		
		try {
			status = restTemplate.postForObject(url, makeTransferEntity(toSend, user.getToken()), String.class);
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		
		return status;
	}
	
	public User[] getAllUsers(AuthenticatedUser user) {
		User[] allUsers = null;
		String url = BASE_URL + "/transfers/userlist";
		
		try {
			allUsers = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), User[].class).getBody();
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return allUsers;
	}
	
	public User getUserById(Long userId, AuthenticatedUser user) {
		User targetUser = null;
		String url = BASE_URL + "/transfers/userlist/" + userId;
		
		try {
			targetUser = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), User.class).getBody();
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return targetUser;
	}
	
	/**
	 * Creates a new request transfer to ask for money 
	 * from one account to the user's
	 * Type should be automatically set to REQUEST
	 * Status should be automatically set to PENDING
	 * 
	 * @param user, amount, requestedAcct
	 * @return new requested transfer
	 */
	public String requestTransfer(BigDecimal amount, Long userToId, AuthenticatedUser user) {
		
		Transfer toSend = makeBasicTransfer(amount, userToId, user);
		toSend.setType(Transfer.Type.REQUEST);
		toSend.setStatus(Transfer.Status.PENDING);
		
		String url = BASE_URL + "transfers/request";
		String status = null;
		
		try {
			status = restTemplate.postForObject(url, makeTransferEntity(toSend, user.getToken()), String.class);		
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		
		return status;
	}
	
	public Transfer[] getTransferHistory(AuthenticatedUser user) {
		Transfer[] transferHistory = null;
		String url = BASE_URL + "/transfers";
		
		try {
			transferHistory = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), Transfer[].class).getBody();
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return transferHistory;
	}
	
	public Transfer getTransferDetails(AuthenticatedUser user, Long transferId) {
		Transfer thisTransfer = null;
		String url = BASE_URL + "/transfers/" + transferId;
		
		try {
			thisTransfer = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), Transfer.class).getBody();
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return thisTransfer;
	}
	
	/**
	 * Checks list of pending requests from database
	 * @param user 
	 * @return pendingRequests
	 */
	public Transfer[] getPendingRequests(AuthenticatedUser user) {
		
		Transfer[] pendingRequests = null;
		
		try {
        	pendingRequests = restTemplate.exchange(BASE_URL + "/transfers/pendingrequests", HttpMethod.GET, makeAuthEntity(user.getToken()), Transfer[].class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
        }
		return pendingRequests;
	}
	
	public String approveRequest(Transfer transfer, AuthenticatedUser user) {
		
		String url = BASE_URL + "/transfers/pendingrequests/" + transfer.getTransferId() + "/approve";
		String outcome = null;
		
		try {
			outcome = restTemplate.exchange(url, HttpMethod.PUT, makeTransferEntity(transfer, user.getToken()), String.class).getBody();
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		return outcome;
	}
	
	public String rejectRequest(Transfer transfer, AuthenticatedUser user) {
		String url = BASE_URL + "/transfers/pendingrequests/" + transfer.getTransferId() + "/reject";
		String outcome = null;
	
		try {
			outcome = restTemplate.exchange(url, HttpMethod.PUT, makeTransferEntity(transfer, user.getToken()), String.class).getBody();
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		return outcome;	
	}
	/*
	public String sendTransfer(BigDecimal amount, Long userToId, AuthenticatedUser user) {
		
		Transfer toSend = makeBasicTransfer(amount, userToId, user);
		toSend.setType(Transfer.Type.SEND);
		toSend.setStatus(Transfer.Status.APPROVED);
		
		String url = BASE_URL + "/transfers/send";
		String status = null;
		
		try {
			status = restTemplate.postForObject(url, makeTransferEntity(toSend, user.getToken()), String.class);
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		
		return status;
	}
	*/ // TODO: remove this after approveRequest complete, only as example
	
	/**
	 * Checks list of pending requests from database
	 * @param user
	 * @return pendingRequests
	 */
	/*
	public Integer approveOrRejectRequest(Request request, int userChoice, AuthenticatedUser user, Principal principal) {
		if (userChoice != 1 || userChoice != 2) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(user.getToken());
		HttpEntity<Integer> entity = new HttpEntity<>(userChoice, headers);
		
		String url = BASE_URL + "requests?append=" + request.getTransferId();		
		
		try {
			restTemplate.put(url, entity);
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return userChoice;
	}
	*/
	
	
	private Transfer makeBasicTransfer(BigDecimal amount, Long userToId, AuthenticatedUser user) {
		Transfer newTransfer = new Transfer();
		newTransfer.setAmount(amount);
		newTransfer.setUserToId(userToId);
		newTransfer.setUserFromId((long)user.getUser().getId());
		
		return newTransfer;
	}
	
	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}
	
}
