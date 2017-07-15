package com.chillies.smartshopper.lib.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DirectoryFiles;
import com.chillies.smartshopper.common.util.EmailValidator;
import com.chillies.smartshopper.lib.exception.ServerConfig;

/**
 * ConfigUtils : is utility class DbConfig.properties.
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Component
@Scope("singleton")
public final class PropertyUtil {

	private static final String APP_CONFIG = "AppConfig.properties";

	@Autowired
	private EmailValidator emailValidator;

	// Database property.
	private String database_url;

	private String database;

	// Mail property.
	private String mail_host;

	private int mail_port;

	private String mail_username;

	private String mail_password;

	@PostConstruct
	private void init() {
		final String configDirectory = DirectoryFiles.CONFIG.getCompletePath();
		final String filePath = configDirectory + AppUtils.PATH_SEPARATOR + APP_CONFIG;

		if (!Files.exists(Paths.get(configDirectory))) {
			throw new ServerConfig(
					String.format("init() Failed :Configration folder is not exists on path %s", configDirectory));
		}

		if (!Files.exists(Paths.get(filePath))) {
			throw new ServerConfig(
					String.format("init() Failed :No %s file exists on path %s", APP_CONFIG, configDirectory));
		}

		final Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(filePath));

			this.database_url = properties.getProperty("database_url");
			this.database = properties.getProperty("database");

			// mail property
			mail_host = properties.getProperty("mail_host");
			mail_port = Integer.parseInt(properties.getProperty("mail_port"));
			mail_username = properties.getProperty("mail_username");
			mail_password = properties.getProperty("mail_password");

			if (!emailValidator.validate(mail_username)) {
				throw new ServerConfig(String.format("%s not valid email address.", mail_username));
			}
		} catch (IOException e) {
			throw new ServerConfig(String.format("init() Failed : %s", e.getMessage()));
		} catch (Exception e) {
			throw new ServerConfig(String.format("init() Failed : %s", e.getMessage()));
		}
	}

	public String getDatabase_url() {
		return database_url;
	}

	public String getDatabase() {
		return database;
	}

	public String getMail_host() {
		return mail_host;
	}

	public int getMail_port() {
		return mail_port;
	}

	public String getMail_username() {
		return mail_username;
	}

	public String getMail_password() {
		return mail_password;
	}

}
