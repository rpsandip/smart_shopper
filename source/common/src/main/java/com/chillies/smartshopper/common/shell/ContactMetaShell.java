package com.chillies.smartshopper.common.shell;

import com.google.common.base.Optional;

public class ContactMetaShell {

	private final String street;

	private final String city;

	private final String state;

	private final String country;

	private final String postalCode;

	private final String phoneNo;

	public ContactMetaShell(final Optional<String> street, final Optional<String> city, final Optional<String> state,
			final Optional<String> country, final Optional<String> postalCode, String phoneNo) {
		this.street = street.orNull();
		this.city = city.orNull();
		this.state = state.orNull();
		this.country = country.orNull();
		this.postalCode = postalCode.orNull();
		this.phoneNo = phoneNo;
	}

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
		builder.append("ContactMetaShell [");
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
