package com.chillies.smartshopper.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.chillies.smartshopper.common.util.AppUtils;

/**
 * WebApp : initializing app for web smaart-shopper App.
 * 
 * @author Jinen Kothari
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.chillies.smartshopper.*")
@EnableAsync
public class WebApp {

	private static final Logger LOG = LoggerFactory.getLogger(WebApp.class);

	public static void main(String[] args) {

		final String homePath = System.getProperty(AppUtils.SMART_SHOPPER);

		if (homePath == null || homePath.isEmpty()) {
			LOG.error("main() {} is not set.", AppUtils.SMART_SHOPPER);
			System.exit(0);
		}
		AppUtils.setHOME(homePath);

		SpringApplication.run(WebApp.class, args);
	}
}
