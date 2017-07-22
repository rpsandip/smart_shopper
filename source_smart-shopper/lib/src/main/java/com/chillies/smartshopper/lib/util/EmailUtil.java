package com.chillies.smartshopper.lib.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DirectoryFiles;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.google.common.base.Preconditions;

/**
 * EmailUtil : is utility class Email.html
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Service
@Scope("singleton")
public class EmailUtil {

	private static final Logger LOG = LoggerFactory.getLogger(EmailUtil.class);

	private static final String EMAIL_CONFIG = "Email.html";

	private static final String USER = "$$$";

	private static final String USERNAME = "${user}";

	private static final String PASSWORD = "${password}";

	public String getEmailTemplete(final String name, final String username, final String password) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");

		final String configDirectory = DirectoryFiles.CONFIG.getCompletePath();
		final String filePath = configDirectory + AppUtils.PATH_SEPARATOR + EMAIL_CONFIG;

		final StringBuilder builder = new StringBuilder();

		try {
			final String content = FileUtils.readFileToString(new File(filePath), AppUtils.CHAR_TYPE)
					.replace(USER, name).replace(USERNAME, username).replace(PASSWORD, password);

			builder.append(content);
			return builder.toString();
		} catch (IOException e) {
			final String error = String.format("getEmailTemplete() Failed for %s  %s", username, e.getMessage());
			LOG.error(error);
			throw new NotAccatable(error);
		}
	}
}