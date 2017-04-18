package com.chillies.smartshopper.lib.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Sudoers : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class Users {

	@Id
	private String id;

	@Indexed(unique = true)
	@TextIndexed
	private String username;

	private String password;

	private String firstName;

	private String lastName;

	private DateMeta dateMeta;

	private String remark;

	@Indexed(unique = true)
	@TextIndexed
	private String referralCode;

	private ContactMeta contactMeta;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public Users() {
	}

	public Users(String username, String password, String firstName, String lastName, DateMeta dateMeta,
			String referralCode, Optional<ContactMeta> contactMeta, Optional<String> remark) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");
		Preconditions.checkNotNull(contactMeta, "contactMeta can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");

		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateMeta = dateMeta;
		this.referralCode = referralCode;
		this.contactMeta = contactMeta.orNull();
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

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public String getRemark() {
		return remark;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public ContactMeta getContactMeta() {
		return contactMeta;
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 3;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		final Users users = (Users) obj;
		return users.id == this.id;
	}

	public UsersShell toShell() {
		return new UsersShell(this.id, this.username, this.firstName, this.lastName, this.dateMeta.toShell(),
				Optional.fromNullable(this.remark), this.referralCode, this.contactMeta.toShell());
	}

}
