package com.chillies.smartshopper.common.shell;

import org.joda.time.DateTime;

import com.chillies.smartshopper.common.util.DateUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class DateMetaShell {

	private final String created;

	private final String updated;

	public DateMetaShell(final DateTime created, final Optional<DateTime> updated) {
		Preconditions.checkNotNull(created, "created can not be null.");
		Preconditions.checkNotNull(updated, "updated can not be null.");
		this.created = DateUtils.toString(created, false);
		this.updated = updated.isPresent() ? DateUtils.toString(updated.get(), false) : null;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

}
