package com.chillies.smartshopper.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ServicesNotFound: is throws exception when Validation fails.
 * 
 * @author Jinen Kothari
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND)  // 404
public class ServicesNotFound extends RuntimeException {

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
	public ServicesNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServicesNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServicesNotFound(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServicesNotFound(Throwable cause) {
		super(cause);
	}

}
