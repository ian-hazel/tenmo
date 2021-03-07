package com.techelevator.tenmo.services;

import java.math.BigDecimal;

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

	public User[] sendTransfer(AuthenticatedUser user) {
		User[] allUsers = null;
		try {
			allUsers = restTemplate.exchange(BASE_URL + "transfers/send", HttpMethod.GET, makeAuthEntity(user.getToken()), User[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return allUsers;
	}
	
	public User[] requestTransfer(AuthenticatedUser user) {
		User[] allUsers = null;
		try {
			allUsers = restTemplate.exchange(BASE_URL + "transfers/request", HttpMethod.GET, makeAuthEntity(user.getToken()), User[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return allUsers;
	}

	/**
	 * Creates a new send transfer to send money from one account to another Type
	 * should be automatically set to SEND Status should be automatically set to
	 * APPROVED
	 * 
	 * @param user, amount, receivingAcct
	 * @return new sent transfer
	 */
	public void sendTransfer(Long userToId, BigDecimal amount, AuthenticatedUser user) {
		Transfer sendTransfer = makeBasicTransfer(userToId, amount);
		try {
			sendTransfer = restTemplate.postForObject(BASE_URL + "transfers/send", makeTransferEntity(sendTransfer, user.getToken()), Transfer.class);
		} catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
	}

	/**
	 * Creates a new request transfer to ask for money from one account to the
	 * user's Type should be automatically set to REQUEST Status should be
	 * automatically set to PENDING
	 * 
	 * @param user, amount, requestedAcct
	 * @return new requested transfer
	 */
	public void requestTransfer(Long userToId, BigDecimal amount,  AuthenticatedUser user) {
		Transfer requestTransfer = makeBasicTransfer(userToId, amount);
		try {
			requestTransfer = restTemplate.postForObject(BASE_URL + "transfers/request", makeTransferEntity(requestTransfer, user.getToken()), Transfer.class);
		} catch (RestClientResponseException e) {
			System.out.println((e.getRawStatusCode() + " : " + e.getResponseBodyAsString()));
		}
	}

	public Transfer[] getTransferHistory(AuthenticatedUser user) {
		Transfer[] transferHistory = null;
		try {
			transferHistory = restTemplate
					.exchange(BASE_URL + "transfers/details", HttpMethod.GET, makeAuthEntity(user.getToken()), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return transferHistory;
	}

	public Transfer getTransferDetails(AuthenticatedUser user, Long transferId) {
		Transfer thisTransfer = null;
		String url = BASE_URL + "transfers/details/" + transferId;
		try {
			thisTransfer = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(user.getToken()), Transfer.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
		return thisTransfer;
	}

	private Transfer makeBasicTransfer(Long userId, BigDecimal amount ) {
		Transfer newTransfer = new Transfer();
		newTransfer.setUserId(userId);
		newTransfer.setAmount(amount);
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
