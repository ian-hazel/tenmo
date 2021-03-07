package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Request;

public class RequestService {
	
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	public RequestService(String url) {
		BASE_URL = url;
	}


	/**
	 * Checks list of pending requests from database
	 * 
	 * @param user
	 * @return pendingRequests
	 */
	public Request[] getPendingRequests(AuthenticatedUser user) {
		Request[] pendingRequests = null;
		try {
			pendingRequests = restTemplate
					.exchange(BASE_URL + "requests", HttpMethod.GET, makeAuthEntity(user.getToken()), Request[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
			ex.printStackTrace();
		}
		return pendingRequests;
	}

	/**
	 * Checks list of pending requests from database
	 * 
	 * @param user
	 * @return pendingRequests
	 */
	public void requestResponse(Long transferId, Long userChoice, AuthenticatedUser user) {
		String url = BASE_URL + "/requests/" + transferId + "?append=" + userChoice;
		try {
			restTemplate.put(url, makeAuthEntity(user.getToken()));
		} catch (RestClientResponseException ex) {
			System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
		}
	}
	
	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
