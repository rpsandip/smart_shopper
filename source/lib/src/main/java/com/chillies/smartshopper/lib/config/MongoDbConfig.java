package com.chillies.smartshopper.lib.config;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.chillies.smartshopper.lib.exception.MongoConfigException;
import com.chillies.smartshopper.lib.util.PropertyUtil;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * MongoDbConfig: is use to connect the database as well scanning basic package
 * as well as Repositories.
 * 
 * Application context for all service will define.
 * 
 * @author Jinen Kothari
 *
 */
@Configuration
@PropertySource(value = { "classpath:context.properties" })
@EnableMongoRepositories(basePackages = "com.chillies.smartshopper.lib.repo")
public class MongoDbConfig extends AbstractMongoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(MongoDbConfig.class);

	@Autowired
	private PropertyUtil propertyUtil;

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws MongoConfigException, UnknownHostException {

		final String databaseUri = propertyUtil.getDatabase_url();
		final MongoClientURI mongoClientURI = new MongoClientURI(databaseUri);

		try {
			mongoClientURI.getCollection();
		} catch (Exception e) {
			LOG.info("mongo() Failed to connect on {}.", databaseUri);
			System.exit(1);
		}
		return new SimpleMongoDbFactory(mongoClientURI);
	}

	@Bean
	public MongoTemplate getMongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());

	}

	@Override
	protected String getDatabaseName() {
		return propertyUtil.getDatabase();
	}

	@Override
	public Mongo mongo() throws Exception {

		final String databaseUri = propertyUtil.getDatabase_url();
		final Mongo mongo = new MongoClient(new MongoClientURI(databaseUri));

		try {
			mongo.getAllAddress();
		} catch (Exception e) {
			LOG.info("mongo() Failed to connect on {}.", databaseUri);
			System.exit(1);
		}
		return mongo;
	}

}
