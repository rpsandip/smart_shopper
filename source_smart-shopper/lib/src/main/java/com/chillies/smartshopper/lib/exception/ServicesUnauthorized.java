package com.chillies.smartshopper.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ServicesUnauthorized: is throws exception when REST is unauthorized.
 * 
 * @author Jinen Kothari
 *
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED) // 401
public class ServicesUnauthorized extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServicesUnauthorized(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServicesUnauthorized(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServicesUnauthorized(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServicesUnauthorized(Throwable cause) {
		super(cause);
	}

}
