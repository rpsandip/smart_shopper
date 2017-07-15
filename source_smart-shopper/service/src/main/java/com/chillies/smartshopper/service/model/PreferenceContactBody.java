package com.chillies.smartshopper.service.model;

import org.hibernate.validator.constraints.NotBlank;

public class PreferenceContactBody {

	private String id;

	@NotBlank(message = "name can not be null.")
	private String name;

	@NotBlank(message = "phoneNo can not be null.")
	private String phoneNo;

	@NotBlank(message = "emailId can not be null.")
	private String emailId;

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
		builder.append("PreferenceContactBody [");
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

}
