package com.chillies.smartshopper.common.shell.web;

import com.google.common.base.Preconditions;

/**
 * AuthShell : is Shell class. which will be send to Launcher or Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * 
 * @author Jinen Kothari
 *
 */
public class UsersAuthShell {

	private final String session;

	private final UsersShell user;

	public UsersAuthShell(final String session, final UsersShell user) {
		Preconditions.checkNotNull(session, "session can not be null.");
		Preconditions.checkNotNull(user, "user can not be null.");

		this.session = session;
		this.user = user;
	}

	public String getSession() {
		return session;
	}

	public UsersShell getUser() {
		return user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthShell [session=").append(session).append(", user=").append(user.toString()).append("]");
		return builder.toString();
	}

}
