package com.chillies.smartshopper.lib.exception;

/**
 * MongoConfig: is throws exception when it occurs from MongoDb configuration.
 * 
 * @author Jinen Kothari
 *
 */
public class MongoConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MongoConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MongoConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public MongoConfigException(String message) {
		super(message);
	}

	public MongoConfigException(Throwable cause) {
		super(cause);
	}

}
