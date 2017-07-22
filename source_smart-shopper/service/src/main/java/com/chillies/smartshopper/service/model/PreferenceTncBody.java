package com.chillies.smartshopper.service.model;

import org.hibernate.validator.constraints.NotBlank;

public class PreferenceTncBody {
	
	@NotBlank(message = "tnc can not be null.")
	private String tnc;

	public String getTnc() {
		return tnc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PreferenceTncBody [");
		if (tnc != null)
			builder.append("tnc=").append(tnc);
		builder.append("]");
		return builder.toString();
	}

}
