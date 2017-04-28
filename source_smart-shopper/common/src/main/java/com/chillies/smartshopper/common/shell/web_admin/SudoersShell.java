package com.chillies.smartshopper.common.shell.web_admin;

import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class SudoersShell {

	private final String id;

	private final String username;

	private final String password;

	private final String firstName;

	private final String lastName;

	private final DateMetaShell dateMeta;

	private final String remark;

	public SudoersShell(String id, String username, String password, String firstName, String lastName,
			DateMetaShell dateMeta, Optional<String> remark) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateMeta = dateMeta;
		this.remark = remark.orNull();
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SudoersShell [id=").append(id).append(", username=").append(username).append(", password=")
				.append(password).append(", firstName=").append(firstName).append(", lastName=").append(lastName)
				.append(", dateMeta=").append(dateMeta).append(", remark=").append(remark).append("]");
		return builder.toString();
	}

}
