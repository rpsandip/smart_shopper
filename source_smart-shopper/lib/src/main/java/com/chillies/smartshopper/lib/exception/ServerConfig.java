package com.chillies.smartshopper.lib.exception;

/**
 * ServerConfig: is throws exception when it occurs when server configration
 * fails.
 * 
 * @author Jinen Kothari
 *
 */
public class ServerConfig extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerConfig(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServerConfig(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerConfig(String message) {
		super(message);
	}

	public ServerConfig(Throwable cause) {
		super(cause);
	}

}
