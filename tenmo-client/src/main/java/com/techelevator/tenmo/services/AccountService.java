package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import com.techelevator.tenmo.models.AuthenticatedUser;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {
	
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	public AccountService(String url) {
		BASE_URL = url;
	}
	
	/**
	  * Returns the balance in BigDecimal from account table using
	  * principal as the key
	  *
	  * @param accountId
	  * @return balance
	  */
	public BigDecimal getBalance(AuthenticatedUser user) {
		BigDecimal balance = null;
		try {
        	balance = restTemplate
                    .exchange(BASE_URL + "/balance" + "/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user.getToken()), BigDecimal.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString()));
        }
        return balance;
	} //end getBalance()

	/**
	 * Returns an {HttpEntity} with the `Authorization: Bearer:` header
	 *
	 * @return {HttpEntity}
	 */
	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

} //end AccountService

