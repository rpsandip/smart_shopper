package com.chillies.smartshopper.lib.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.SudoersShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Sudoers : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class Sudoers {

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

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public Sudoers() {
	}

	public Sudoers(String username, String password, String firstName, String lastName, DateMeta dateMeta,
			Optional<String> remark) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");

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

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public String getRemark() {
		return remark;
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
		final Sudoers sudoers = (Sudoers) obj;
		return sudoers.id == this.id;
	}

	public SudoersShell toShell() {

		return new SudoersShell(this.id, this.username, this.password, this.firstName, this.lastName,
				this.dateMeta.toShell(), Optional.fromNullable(this.remark));
	}

}
