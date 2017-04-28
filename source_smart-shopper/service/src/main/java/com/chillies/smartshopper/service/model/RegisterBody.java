package com.chillies.smartshopper.service.model;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

/**
 * LoginBody : is request body object that Application will consume.
 * 
 * Validation of field and If Date is there it will be taken as yyyyMMdd or
 * yyyyMMdd HH:mm:ss Z
 * 
 * @author Jinen Kothari
 *
 */
public class RegisterBody {

	@NotBlank(message = "username can not be null.")
	private String username;

	@NotBlank(message = "password can not be null.")
	private String password;

	@NotBlank(message = "firstName can not be null.")
	private String firstName;

	@NotBlank(message = "lastName can not be null.")
	private String lastName;

	private String remark;

	@Valid
	private ContactBody contactBody;

	private String parentId;

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

	public String getRemark() {
		return remark;
	}

	public ContactBody getContactBody() {
		return contactBody;
	}

	public String getParentId() {
		return parentId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterBody [");
		if (username != null)
			builder.append("username=").append(username).append(", ");
		if (password != null)
			builder.append("password=").append(password).append(", ");
		if (firstName != null)
			builder.append("firstName=").append(firstName).append(", ");
		if (lastName != null)
			builder.append("lastName=").append(lastName).append(", ");
		if (remark != null)
			builder.append("remark=").append(remark).append(", ");
		if (contactBody != null)
			builder.append("contactBody=").append(contactBody).append(", ");
		if (parentId != null)
			builder.append("parentId=").append(parentId);
		builder.append("]");
		return builder.toString();
	}

}
