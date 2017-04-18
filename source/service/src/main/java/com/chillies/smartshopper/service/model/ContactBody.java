package com.chillies.smartshopper.service.model;

import org.hibernate.validator.constraints.NotBlank;

public class ContactBody {

	private String street;

	private String city;

	private String state;

	private String country;

	private String postalCode;

	@NotBlank(message = "phoneNo can not be null.")
	private String phoneNo;

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContactBody [");
		if (street != null)
			builder.append("street=").append(street).append(", ");
		if (city != null)
			builder.append("city=").append(city).append(", ");
		if (state != null)
			builder.append("state=").append(state).append(", ");
		if (country != null)
			builder.append("country=").append(country).append(", ");
		if (postalCode != null)
			builder.append("postalCode=").append(postalCode).append(", ");
		if (phoneNo != null)
			builder.append("phoneNo=").append(phoneNo);
		builder.append("]");
		return builder.toString();
	}

}
