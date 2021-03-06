package com.techelevator.tenmo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot find Transfer.")
public class TransferNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3893897073990438497L;

}
