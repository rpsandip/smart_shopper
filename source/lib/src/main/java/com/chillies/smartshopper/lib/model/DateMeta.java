package com.chillies.smartshopper.lib.model;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.Indexed;

import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.util.DateUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class DateMeta {

	@Indexed
	private DateTime created;

	private DateTime updated;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public DateMeta() {
	}

	public DateMeta(final Optional<DateTime> updated) {
		Preconditions.checkNotNull(updated, "updated can not null.");

		this.created = DateUtils.currentUTC();
		this.updated = updated.orNull();
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public void setUpdated(final DateTime updated) {
		Preconditions.checkNotNull(updated, "updated can not null.");
		this.updated = updated;
	}

	public DateMetaShell toShell() {
		return new DateMetaShell(this.created, Optional.fromNullable(this.updated));
	}
}
