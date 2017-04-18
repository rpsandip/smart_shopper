package com.chillies.smartshopper.service.model;

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
public class LoginBody {

	@NotBlank(message = "username can not be null.")
	private String username;

	@NotBlank(message = "password can not be null.")
	private String password;

	public String getUsername() {
		return username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginBody [username=").append(username).append(", password=").append(password).append("]");
		return builder.toString();
	}

}
