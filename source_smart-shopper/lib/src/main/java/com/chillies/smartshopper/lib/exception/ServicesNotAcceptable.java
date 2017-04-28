package com.chillies.smartshopper.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ServicesNotAcceptable: is throws exception when Validation fails.
 * 
 * @author Jinen Kothari
 *
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE) // 406
public class ServicesNotAcceptable extends RuntimeException {

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
	public ServicesNotAcceptable(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServicesNotAcceptable(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServicesNotAcceptable(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServicesNotAcceptable(Throwable cause) {
		super(cause);
	}

}
