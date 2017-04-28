package com.chillies.smartshopper.common.shell;

import com.chillies.smartshopper.common.shell.web_admin.SudoersShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class ActivateShell {

	private final boolean isActivate;

	private final SudoersShell updatedBy;

	public ActivateShell(final boolean isActivate, final Optional<SudoersShell> updatedBy) {
		Preconditions.checkNotNull(updatedBy, "updatedBy can not be null.");
		this.isActivate = isActivate;
		this.updatedBy = updatedBy.orNull();
	}

	public SudoersShell getUpdatedBy() {
		return updatedBy;
	}

	public boolean isActivate() {
		return isActivate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivateShell [isActivate=").append(isActivate).append(", ");
		if (updatedBy != null)
			builder.append("updatedBy=").append(updatedBy);
		builder.append("]");
		return builder.toString();
	}

}
