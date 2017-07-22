package com.chillies.smartshopper.lib.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.chillies.smartshopper.lib.util.PropertyUtil;

/**
 * MailConfig: is configuration of mail set-up.
 * 
 * @author Jinen Kothari
 *
 */
@Configuration
public class MailConfig {

	@Autowired
	private PropertyUtil propertyUtil;

	@Bean
	public JavaMailSender javaMailService() {

		final String host = propertyUtil.getMail_host();
		final int port = propertyUtil.getMail_port();
		final String username = propertyUtil.getMail_username();
		final String password = propertyUtil.getMail_password();

		final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);

		javaMailSender.setJavaMailProperties(getMailProperties());

		return javaMailSender;
	}

	private Properties getMailProperties() {

		final String host = propertyUtil.getMail_host();

		final Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", host);

		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.ssl.trust", host);
		properties.setProperty("mail.debug", "false");
		return properties;
	}
}