package com.chillies.smartshopper.common.shell.web_admin;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * ProductCategoryShell : is Shell class. which will be send to Launcher or Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * 
 * @author Jinen Kothari
 *
 */
public class ProductCategoryShell {

	private final String id;

	private final String name;

	private final String remark;

	private final DateMetaShell dateMeta;

	private final CreatedMetaShell createdMeta;

	public ProductCategoryShell(String id, String name, Optional<String> remark, DateMetaShell dateMeta,
			CreatedMetaShell createdMeta) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");
		this.id = id;
		this.name = name;
		this.remark = remark.orNull();
		this.dateMeta = dateMeta;
		this.createdMeta = createdMeta;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public DateMetaShell getDateMeta() {
		return dateMeta;
	}

	public CreatedMetaShell getCreatedMeta() {
		return createdMeta;
	}

}