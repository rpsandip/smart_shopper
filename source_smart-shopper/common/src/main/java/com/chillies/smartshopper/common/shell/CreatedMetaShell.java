package com.chillies.smartshopper.common.shell;

import com.chillies.smartshopper.common.shell.web_admin.SudoersShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * CreatedMetaShell : is Shell class. which will be send to Launcher or Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * 
 * @author Jinen Kothari
 *
 */
public class CreatedMetaShell {

	private SudoersShell created;

	private SudoersShell updated;

	public CreatedMetaShell(final SudoersShell created, final Optional<SudoersShell> updated) {
		Preconditions.checkNotNull(created, "created can not be null.");
		Preconditions.checkNotNull(updated, "updated can not be null.");
		this.created = created;
		this.updated = updated.orNull();
	}

	public SudoersShell getCreated() {
		return created;
	}

	public SudoersShell getUpdated() {
		return updated;
	}

}
