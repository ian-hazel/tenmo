package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.security.Principal;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	public TransferService(String url) {
		BASE_URL = url;
	}
	
	/**
	  * Returns the balance in BigDecimal from account table using
	  * principal as the key
	  *
	  * @param accountId
	  * @return balance
	  */
	/*
	public BigDecimal getBalance(AuthenticatedUser user) {
		BigDecimal balance = null;
		try {
       	balance = restTemplate
                   .exchange(BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(user.getToken()), BigDecimal.class).getBody();
       } catch (RestClientResponseException ex) {
           System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
       }
       return balance;
	}
	*/
	
	/**
	 * Creates a new send transfer to send money 
	 * from one account to another
	 * Type should be automatically set to SEND
	 * Status should be automatically set to APPROVED
	 * 
	 * @param user, amount, receivingAcct
	 * @return new sent transfer
	 */
	public Transfer sendTransfer(AuthenticatedUser user, BigDecimal amount, Account receivingAcct) {
		//int sendingUserAcct = user.getUser().getId();
		// TODO: need to get the authenticated user's acct
		
		String url = BASE_URL + "/transfers";
		
		Transfer sendTransfer = makeBasicTransfer(amount, get_user_acct, receivingAcct);
		sendTransfer.setType(Transfer.Type.SEND);
		sendTransfer.setStatus(Transfer.Status.APPROVED);
		
		try {
			// TODO: send transfer to server as POST method
		}
		catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
		
		return sendTransfer;
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
		Transfer requestTransfer = makeBasicTransfer(amount, get_user_acct, requestedAcct);
		requestTransfer.setType(Transfer.Type.REQUEST);
		requestTransfer.setStatus(Transfer.Status.PENDING);
		
		return requestTransfer;
	}
	
	
	
	private Transfer makeBasicTransfer(BigDecimal amount, Account userAcct, Account receivingAcct) {
		Transfer newTransfer = new Transfer();
		newTransfer.setAcctFromId(userAcct.getAccountId());
		newTransfer.setAcctToId(receivingAcct.getAccountId());
		newTransfer.setAmount(amount);
		
		return newTransfer;
	}
	
	
	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
}
