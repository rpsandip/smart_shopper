package com.chillies.smartshopper.lib.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.web_admin.SudoersShell;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * CreatedMeta : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public class CreatedMeta {

	@DBRef
	private Sudoers created;

	@DBRef
	private Sudoers updated;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public CreatedMeta() {
	}

	public CreatedMeta(final Sudoers created, final Optional<Sudoers> updated) {
		Preconditions.checkNotNull(created, "created can not be null.");
		Preconditions.checkNotNull(created, "created can not be null.");
		this.created = created;
		this.updated = updated.orNull();
	}

	public Sudoers getCreated() {
		return created;
	}

	public Sudoers getUpdated() {
		return updated;
	}

	public void setUpdated(Sudoers updated) {
		Preconditions.checkNotNull(updated, "updated can not be null.");
		this.updated = updated;
	}

	public CreatedMetaShell toShell() {

		final Optional<SudoersShell> updated = this.updated != null ? Optional.of(this.updated.toShell())
				: Optional.absent();
		return new CreatedMetaShell(this.created.toShell(), updated);
	}

}
