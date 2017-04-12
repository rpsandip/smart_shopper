package com.hk.ss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Service Not Found") // 404
public class ServiceUnauthorized extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3211075333916488742L;

	/**
	 * 
	 */
	public ServiceUnauthorized() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceUnauthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceUnauthorized(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ServiceUnauthorized(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ServiceUnauthorized(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
