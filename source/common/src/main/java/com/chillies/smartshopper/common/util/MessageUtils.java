package com.chillies.smartshopper.common.util;

import com.google.common.base.Preconditions;

public final class MessageUtils {

	public static final String USER_N_PASSWORD_INCORRECT = "Username or password is incorrect.";

	public static final String USERNAME_ALREADY_EXISTS = "Username alredy exists.";

	public static final String PHONE_NO_ASSOCIATED_WITH_OTHER_USERNAME = "Phone no is associated with other username.";

	public static final String EMAIL_NOT_VALID = "Email address is not valid.";

	// admin user
	public static String userNotRegistered(final String username) {
		Preconditions.checkNotNull(username, "Please enter username.");

		return String.format("Username %s is not registered.", username);
	}

}
