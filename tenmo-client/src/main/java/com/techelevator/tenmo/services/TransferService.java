package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Request;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
	public Transfer sendTransfer(BigDecimal amount, Long userToId, AuthenticatedUser user, Principal principal) {
		
		Transfer toSend = makeBasicTransfer(amount, userToId, principal);
		toSend.setType(Transfer.Type.SEND);
		toSend.setStatus(Transfer.Status.APPROVED);
		
		String url = BASE_URL + "transfers/";		
		Transfer confirmed = null;
		
		try {
			confirmed = restTemplate.postForObject(url, makeTransferEntity(toSend, user.getToken()), Transfer.class);		
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		
		return confirmed;
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
	public Transfer requestTransfer(AuthenticatedUser user, BigDecimal amount, Account requestedAcct) {
		
		// TODO: need to get the authenticated user's acct
		//Transfer requestTransfer = makeBasicTransfer(amount, get_user_acct, requestedAcct);
		//requestTransfer.setType(Transfer.Type.REQUEST);
		//requestTransfer.setStatus(Transfer.Status.PENDING);
		
		return null;
	}
	
	public List<Transfer> getTransferHistory(Principal principal, AuthenticatedUser user) {
		List<Transfer> transferHistory = null;
		String url = BASE_URL + "transfers/";
		
		try {
			transferHistory = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), List.class).getBody();
		}
		catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return transferHistory;
	}
	
	public Transfer getTransferDetails(Principal principal, AuthenticatedUser user, Long transferId) {
		Transfer thisTransfer = null;
		String url = BASE_URL + "transfers/" + transferId;
		
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
	public List<Request> getPendingRequests(AuthenticatedUser user) {
		List<Request> pendingRequests = null;
		
		try {
        	pendingRequests = restTemplate.exchange(BASE_URL + "/requests", HttpMethod.GET, makeAuthEntity(user.getToken()), List.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
        }
		return pendingRequests;
	}
	
	
	
	private Transfer makeBasicTransfer(BigDecimal amount, Long userToId, Principal principal) {
		// takes in amount, userToId, principal
		Transfer newTransfer = new Transfer();
		newTransfer.setAmount(amount);
		newTransfer.setUserToId(userToId);
		newTransfer.setPrincipal(principal);
		
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
