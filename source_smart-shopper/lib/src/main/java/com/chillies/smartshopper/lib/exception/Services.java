package com.chillies.smartshopper.lib.exception;

/**
 * Services: is throws exception when it occurres from REST API.
 * 
 * @author Jinen Kothari
 *
 */
public class Services extends Exception {

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
	public Services(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public Services(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Services(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Services(Throwable cause) {
		super(cause);
	}

}
