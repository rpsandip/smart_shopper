package com.chillies.smartshopper.lib.model.web_model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.PreferenceTncShell;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * PreferenceTnc : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class PreferenceTnc {

	@Id
	private String id;

	private String tnc;

	private DateMeta dateMeta;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public PreferenceTnc() {
	}

	public PreferenceTnc(String tnc) {
		Preconditions.checkNotNull(tnc, "tnc can not be null.");
		this.tnc = tnc;
		this.dateMeta = new DateMeta(Optional.absent());
	}
	
	public void update(String tnc) {
		Preconditions.checkNotNull(tnc, "tnc can not be null.");
		this.tnc = tnc;
		this.dateMeta.setUpdated(DateUtils.currentUTC());
	}

	public String getId() {
		return id;
	}

	public String getTnc() {
		return tnc;
	}

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public PreferenceTncShell toShell() {
		return new PreferenceTncShell(this.id, this.tnc);
	}
}
