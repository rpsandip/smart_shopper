package com.chillies.smartshopper.common.shell;

import com.google.common.base.Preconditions;

/**
 * PreferenceContactShell : is Shell class. which will be send to Launcher or
 * Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * @author Jinen Kothari
 *
 */
public class PreferenceContactShell {

	private final String id;

	private final String name;

	private final String phoneNo;

	private final String emailId;

	public PreferenceContactShell(String id, String name, String phoneNo, String emailId) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		Preconditions.checkNotNull(emailId, "emailId can not be null.");
		this.id = id;
		this.name = name;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getEmailId() {
		return emailId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PreferenceContactShell [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (phoneNo != null)
			builder.append("phoneNo=").append(phoneNo).append(", ");
		if (emailId != null)
			builder.append("emailId=").append(emailId);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this.getClass() == obj.getClass()) {
			return true;
		}
		final PreferenceContactShell preferenceContact = (PreferenceContactShell) obj;
		return preferenceContact.id == this.id;
	}

}
