package com.chillies.smartshopper.lib.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.lib.exception.NotAccatable;

/**
 * 
 * IMailService : is sending mail from server.
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Service
@Scope("singleton")
public interface IMailService {

	/**
	 * Send Registration : will send email synchronized mail from server.
	 */
	public void sendRegistration(final String name, final String username, final String password)
			throws NotAccatable;

	/**
	 * Async Send Registration : will send email Async mail from server.
	 */
	public void asyncSendRegistration(final String name, final String username, final String password) throws NotAccatable;

}
