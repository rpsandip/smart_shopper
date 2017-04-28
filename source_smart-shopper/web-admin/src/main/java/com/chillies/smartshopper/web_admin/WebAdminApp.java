package com.chillies.smartshopper.web_admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.chillies.smartshopper.common.util.AppUtils;

/**
 * WebAdminApp : initializing app for web admin smaart-shopper App.
 * 
 * @author Jinen Kothari
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.chillies.smartshopper.*")
public class WebAdminApp {

	private static final Logger LOG = LoggerFactory.getLogger(WebAdminApp.class);

	public static void main(String[] args) {

		final String homePath = System.getProperty(AppUtils.SMART_SHOPPER);

		if (homePath == null || homePath.isEmpty()) {
			LOG.error("main() {} is not set.", AppUtils.SMART_SHOPPER);
			System.exit(0);
		}
		AppUtils.setHOME(homePath);

		SpringApplication.run(WebAdminApp.class, args);
	}
}
