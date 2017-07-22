package com.chillies.smartshopper.lib.model.web_model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * PreferenceContact : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class PreferenceContact {

	@Id
	private String id;

	private String name;

	private String phoneNo;

	private String emailId;

	private CreatedMeta createdMeta;

	private DateMeta dateMeta;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public PreferenceContact() {
	}

	public PreferenceContact(String name, String phoneNo, String emailId, Sudoers sudoers) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		Preconditions.checkNotNull(emailId, "emailId can not be null.");
		this.name = name;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.createdMeta = new CreatedMeta(sudoers, Optional.absent());
		this.dateMeta = new DateMeta(Optional.absent());
	}

	public void update(String name, String phoneNo, String emailId, Sudoers sudoers) {

		this.name = name;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.createdMeta.setUpdated(sudoers);
		this.dateMeta.setUpdated(DateUtils.currentUTC());
	}

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

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public CreatedMeta getCreatedMeta() {
		return createdMeta;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PreferenceContact [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (phoneNo != null)
			builder.append("phoneNo=").append(phoneNo).append(", ");
		if (emailId != null)
			builder.append("emailId=").append(emailId).append(", ");
		if (createdMeta != null)
			builder.append("createdMeta=").append(createdMeta).append(", ");
		if (dateMeta != null)
			builder.append("dateMeta=").append(dateMeta);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this.getClass() == obj.getClass()) {
			return true;
		}
		final PreferenceContact preferenceContact = (PreferenceContact) obj;
		return preferenceContact.id == this.id;
	}

	public PreferenceContactShell toShell() {
		return new PreferenceContactShell(this.id, this.name, this.phoneNo, this.emailId);
	}
}
