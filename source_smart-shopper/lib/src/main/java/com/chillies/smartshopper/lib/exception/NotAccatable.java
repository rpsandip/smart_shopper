package com.chillies.smartshopper.lib.exception;

/**
 * NotAccatable: is throws exception when it occurs when Id, Name & validation
 * fails.
 * 
 * @author Jinen Kothari
 *
 */
public class NotAccatable extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAccatable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotAccatable(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAccatable(String message) {
		super(message);
	}

	public NotAccatable(Throwable cause) {
		super(cause);
	}
}
