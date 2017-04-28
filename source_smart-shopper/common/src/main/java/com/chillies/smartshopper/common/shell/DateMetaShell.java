package com.chillies.smartshopper.common.shell;

import org.joda.time.DateTime;

import com.chillies.smartshopper.common.util.DateUtils;
import com.google.common.base.Optional;

public class DateMetaShell {

	private final String created;

	private final String updated;

	public DateMetaShell(final DateTime created, final Optional<DateTime> updated) {
		this.created = DateUtils.toString(created);
		this.updated = updated.isPresent() ? DateUtils.toString(updated.get()) : null;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

}
