package com.github.therycn.tyweatherwebflux.exception;

/**
 * Failure not checked exception.
 * 
 * @author TheryLeopard
 *
 */
public class FailureException extends RuntimeException {

	public FailureException(String message) {
		super(message);
	}

	public FailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
