package com.chillies.smartshopper.lib.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DirectoryFiles;
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

	// Database property.
	private String database_url;

	private String database;

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

}
