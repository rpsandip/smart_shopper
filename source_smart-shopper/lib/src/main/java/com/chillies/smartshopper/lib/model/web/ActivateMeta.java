package com.chillies.smartshopper.lib.model.web;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.chillies.smartshopper.common.shell.ActivateShell;
import com.chillies.smartshopper.common.shell.web_admin.SudoersShell;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * ActivateMeta : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public class ActivateMeta {

	private boolean isActivate;

	@DBRef
	private Sudoers updatedBy;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public ActivateMeta() {
	}

	public ActivateMeta(final boolean isActivate, final Optional<Sudoers> updatedBy) {
		Preconditions.checkNotNull(updatedBy, "updatedBy can not be null.");
		this.isActivate = isActivate;
		this.updatedBy = updatedBy.orNull();
	}

	public boolean isActivate() {
		return isActivate;
	}

	public Sudoers getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivateMeta [isActivate=").append(isActivate).append(", ");
		if (updatedBy != null)
			builder.append("updatedBy=").append(updatedBy);
		builder.append("]");
		return builder.toString();
	}

	public ActivateShell toShell() {
		final Optional<SudoersShell> optional = this.updatedBy != null ? Optional.of(this.updatedBy.toShell())
				: Optional.absent();
		return new ActivateShell(this.isActivate, optional);
	}

}
