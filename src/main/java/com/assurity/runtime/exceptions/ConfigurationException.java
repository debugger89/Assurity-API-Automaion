package com.assurity.runtime.exceptions;

/**
 * Custom exception for any test configuration failures.
 * 
 * @author cdushmantha
 *
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1308630891681332072L;

	public ConfigurationException(String message, Throwable t) {
		super(message, t);
	}

}
