package com.chillies.smartshopper.lib.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import com.chillies.smartshopper.common.shell.ContactMetaShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class ContactMeta {

	private String street;

	private String city;

	private String state;

	private String country;

	private String postalCode;

	@Indexed(unique = true)
	@TextIndexed
	private String phoneNo;

	/**
	 * Db use Only.
	 *
	 * @deprecated
	 */
	public ContactMeta() {
	}

	public ContactMeta(Optional<String> street, Optional<String> city, Optional<String> state, Optional<String> country,
			Optional<String> postalCode, String phoneNo) {
		Preconditions.checkNotNull(street, "street can not be null.");
		Preconditions.checkNotNull(city, "city can not be null.");
		Preconditions.checkNotNull(state, "state can not be null.");
		Preconditions.checkNotNull(country, "country can not be null.");
		Preconditions.checkNotNull(postalCode, "postalCode can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");

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

	public ContactMetaShell toShell() {
		return new ContactMetaShell(Optional.fromNullable(this.street), Optional.fromNullable(this.city),
				Optional.fromNullable(this.state), Optional.fromNullable(this.country),
				Optional.fromNullable(this.postalCode), phoneNo);
	}

}
