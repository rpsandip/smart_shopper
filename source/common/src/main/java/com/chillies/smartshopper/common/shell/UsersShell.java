package com.chillies.smartshopper.common.shell;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class UsersShell {

	private final String id;

	private final String username;

	private final String firstName;

	private final String lastName;

	private final DateMetaShell dateMeta;

	private final String remark;

	private final String referralCode;

	private final ContactMetaShell contactMeta;

	public UsersShell(final String id, final String username, final String firstName, final String lastName,
			final DateMetaShell dateMeta, final Optional<String> remark, final String referralCode,
			final ContactMetaShell contactMeta) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");
		Preconditions.checkNotNull(contactMeta, "contactMeta can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");

		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateMeta = dateMeta;
		this.remark = remark.orNull();
		this.referralCode = referralCode;
		this.contactMeta = contactMeta;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public DateMetaShell getDateMeta() {
		return dateMeta;
	}

	public String getRemark() {
		return remark;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public ContactMetaShell getContactMeta() {
		return contactMeta;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UsersShell [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (username != null)
			builder.append("username=").append(username).append(", ");
		if (firstName != null)
			builder.append("firstName=").append(firstName).append(", ");
		if (lastName != null)
			builder.append("lastName=").append(lastName).append(", ");
		if (dateMeta != null)
			builder.append("dateMeta=").append(dateMeta).append(", ");
		if (remark != null)
			builder.append("remark=").append(remark).append(", ");
		if (referralCode != null)
			builder.append("referralCode=").append(referralCode).append(", ");
		if (contactMeta != null)
			builder.append("contactMeta=").append(contactMeta);
		builder.append("]");
		return builder.toString();
	}

}
