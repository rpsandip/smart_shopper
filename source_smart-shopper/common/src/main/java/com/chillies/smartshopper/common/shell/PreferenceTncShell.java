package com.chillies.smartshopper.common.shell;

import com.google.common.base.Preconditions;

/**
 * PreferenceTncShell : is Shell class. which will be send to Launcher or Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * @author Jinen Kothari
 *
 */
public class PreferenceTncShell {

	private final String id;

	private final String tnc;

	public PreferenceTncShell(final String id, final String tnc) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(tnc, "tnc can not be null.");
		this.id = id;
		this.tnc = tnc;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTnc() {
		return tnc;
	}
}
