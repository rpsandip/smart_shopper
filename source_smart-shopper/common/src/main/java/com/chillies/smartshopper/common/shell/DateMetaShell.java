package com.chillies.smartshopper.common.shell;

import org.joda.time.DateTime;

import com.chillies.smartshopper.common.util.DateUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class DateMetaShell {

	private final String created;

	private final DateTime createdDate;

	private final String updated;

	private final DateTime updatedDate;

	public DateMetaShell(final DateTime created, final Optional<DateTime> updated) {
		Preconditions.checkNotNull(created, "created can not be null.");
		Preconditions.checkNotNull(updated, "updated can not be null.");
		this.createdDate = created;
		this.created = DateUtils.toString(created, false);
		this.updatedDate = updated.orNull();
		this.updated = updated.isPresent() ? DateUtils.toString(updated.get(), false) : null;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public String getCreated() {
		return created;
	}

	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdated() {
		return updated;
	}

}
