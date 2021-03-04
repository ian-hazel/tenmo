package com.techelevator.tenmo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST, reason = "Cannot find Account.")
public class AccountDoesNotExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1231025268206293267L;

}
