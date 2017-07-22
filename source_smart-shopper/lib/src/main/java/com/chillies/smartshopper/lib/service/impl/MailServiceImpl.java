package com.chillies.smartshopper.lib.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.service.IMailService;
import com.chillies.smartshopper.lib.util.EmailUtil;
import com.chillies.smartshopper.lib.util.PropertyUtil;
import com.google.common.base.Preconditions;

/**
 * 
 * MailServiceImpl : is is implementation of MailService.
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Service
@Scope("singleton")
final class MailServiceImpl implements IMailService {

	private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

	private static final String CONTENT_TYPE = "text/html";

	@Autowired
	private EmailUtil emailUtil;

	//
	@Autowired
	private JavaMailSender javaMailService;

	@Autowired
	private PropertyUtil propertyUtil;

	/**
	 * @param to
	 * @param name
	 */
	@Override
	public void sendRegistration(final String name, final String username, final String password) throws NotAccatable {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");

		try {

			LOG.info("Mail started sending on {}", username);

			final MimeMessage message = content(name, username, password);
			javaMailService.send(message);

			LOG.info("Mail hase been sent to {}", username);

		} catch (Exception e) {
			final String error = String.format("Failed to send Mail on  %s. %s", username, e.getMessage());
			LOG.error(error);
			throw new NotAccatable(String.format("Failed to send Mail on  %s .", username));
		}
	}

	@Override
	@Async
	public void asyncSendRegistration(final String name, final String username, final String password)
			throws NotAccatable {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");

		try {

			LOG.info("Mail started sending on {}", username);

			final MimeMessage message = content(name, username, password);
			javaMailService.send(message);

			LOG.info("Mail hase been sent to {}", username);

		} catch (Exception e) {
			final String error = String.format("Failed to send Mail on  %s. %s", username, e.getMessage());
			LOG.error(error);
			throw new NotAccatable(String.format("Failed to send Mail on  %s .", username));
		}
	}

	private MimeMessage content(final String name, final String username, final String password)
			throws MessagingException {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");

		final MimeMessage message = javaMailService.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message, false, AppUtils.CHAR_TYPE);

		helper.setFrom(propertyUtil.getMail_username());
		helper.setTo(username);
		helper.setSubject("Smart shoppers fotgot password");

		final String content = emailUtil.getEmailTemplete(name, username, password);

		message.setContent(String.format("%s", content), CONTENT_TYPE);

		return message;
	}

}
