package com.chillies.smartshopper.lib.model.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.lib.model.ContactMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
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

	private String point;

	@Indexed(unique = true)
	@TextIndexed
	private String referralCode;

	private ContactMeta contactMeta;

	@DBRef
	private Set<Users> subUsers;

	private ActivateMeta activateMeta;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public Users() {
	}

	public Users(String username, String password, String firstName, String lastName, DateMeta dateMeta,
			String referralCode, Optional<ContactMeta> contactMeta, Optional<String> remark,
			Optional<Set<Users>> optionalUsers) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");
		Preconditions.checkNotNull(contactMeta, "contactMeta can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(optionalUsers, "optionalUsers can not be null.");

		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateMeta = dateMeta;
		this.referralCode = referralCode;
		this.contactMeta = contactMeta.orNull();
		this.remark = remark.orNull();
		this.point = "0.0";
		this.subUsers = optionalUsers.orNull();
		this.activateMeta = new ActivateMeta(true, Optional.absent());
	}

	public void addSubUsers(final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");

		this.dateMeta.setUpdated(DateUtils.currentUTC());
		final Set<Users> tempUsers = new HashSet<>();
		tempUsers.add(users);

		if (this.subUsers != null) {
			tempUsers.addAll(this.subUsers);
		}
		this.subUsers = tempUsers;
	}

	public void activateUser(final Sudoers updatedBy) {
		Preconditions.checkNotNull(updatedBy, "updatedBy");
		this.activateMeta = new ActivateMeta(true, Optional.of(updatedBy));
		this.dateMeta.setUpdated(DateUtils.currentUTC());
	}

	public void addPoints(final double newPoints) {
		double tempPoint = AppUtils.stringToDouble(point);
		tempPoint += newPoints;
		this.point = AppUtils.doubleToString(tempPoint);
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

	public String getPoint() {
		return point;
	}

	public double point() {
		return AppUtils.stringToDouble(point);
	}

	public String getReferralCode() {
		return referralCode;
	}

	public ContactMeta getContactMeta() {
		return contactMeta;
	}

	public Set<Users> getSubUsers() {
		return subUsers;
	}

	public ActivateMeta getActivateMeta() {
		return activateMeta;
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
		final Set<UsersShell> usersShells = new HashSet<>();
		if (this.subUsers != null) {
			this.subUsers.forEach(sub -> usersShells.add(sub.toShell()));
		}
		return new UsersShell(this.id, this.username, this.firstName, this.lastName, this.dateMeta.toShell(),
				Optional.fromNullable(this.remark), this.referralCode, this.contactMeta.toShell(),
				Optional.fromNullable(usersShells), this.activateMeta.toShell(), this.point);
	}

}
