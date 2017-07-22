package com.chillies.smartshopper.common.shell.web_admin;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.util.SuperCategory;
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

	private final SuperCategory superCategory;

	private final String name;

	private final String remark;

	private final DateMetaShell dateMeta;

	private final CreatedMetaShell createdMeta;

	private final boolean deleted;

	public ProductCategoryShell(SuperCategory superCategory, String id, String name, Optional<String> remark,
			DateMetaShell dateMeta, CreatedMetaShell createdMeta, final boolean deleted) {
		Preconditions.checkNotNull(superCategory, "superCategory can not be null.");
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");
		this.id = id;
		this.superCategory = superCategory;
		this.name = name;
		this.remark = remark.orNull();
		this.dateMeta = dateMeta;
		this.createdMeta = createdMeta;
		this.deleted = deleted;
	}

	public String getId() {
		return id;
	}

	public SuperCategory getSuperCategory() {
		return superCategory;
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

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		final ProductCategoryShell categoryProduct = (ProductCategoryShell) obj;
		return categoryProduct.id == this.id;
	}

}
