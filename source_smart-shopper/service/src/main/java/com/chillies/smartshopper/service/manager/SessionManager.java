package com.chillies.smartshopper.service.manager;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.exception.ServicesUnauthorized;
import com.google.common.base.Preconditions;

/**
 * SessionManager :is manager for site-user when it logged in it will matain
 * there session.
 * 
 * @author Jinen Kothari
 *
 */
@Service
public class SessionManager {

	private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);

	private final LinkedHashMap<String, String> sessions = new LinkedHashMap<>();

	public void add(final String key, final String value) {
		Preconditions.checkNotNull(key, "key can not be null.");
		Preconditions.checkNotNull(value, "value can not be null.");

		if (sessions.containsKey(key)) {
			return;
		}
		sessions.put(key, value);
		LOG.info(String.format("add() key %s with user %s", key, value));
	}

	public void remove(final String key) {
		Preconditions.checkNotNull(key, "key can not be null.");

		if (!sessions.containsKey(key)) {
			return;
		}

		sessions.remove(key);
		LOG.info(String.format("remove() key %s", key));
	}

	private String getValue(final String key) {
		Preconditions.checkNotNull(key, "key can not be null.");

		return sessions.get(key);
	}

	public String getKey(final String value) {
		Preconditions.checkNotNull(value, "value can not be null.");

		for (Entry<String, String> entry : sessions.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	
	public String getSessionUser(final String session) throws DbException, ServicesNotFound {
		Preconditions.checkNotNull(session, "session can not be null.");

		final String user = this.getValue(session);

		if (user == null) {
			throw new ServicesUnauthorized(MessageUtils.USER_SESSION_EXPIRED);
		}
		return user;
	}
}