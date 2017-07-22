package com.chillies.smartshopper.lib.model.web_model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.common.util.SuperCategory;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * ProductCategory : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class ProductCategory {

	@Id
	private String id;

	private SuperCategory superCategory;

	@Indexed(unique = true)
	@TextIndexed
	private String name;

	private String remark;

	private DateMeta dateMeta;

	private CreatedMeta createdMeta;

	private boolean deleted;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public ProductCategory() {
	}

	public ProductCategory(SuperCategory superCategory, String name, Optional<String> remark, DateMeta dateMeta,
			CreatedMeta createdMeta) {
		Preconditions.checkNotNull(superCategory, "superCategory can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");
		this.superCategory = superCategory;
		this.name = name;
		this.remark = remark.orNull();
		this.dateMeta = dateMeta;
		this.createdMeta = createdMeta;
		this.deleted = false;
	}

	public void update(SuperCategory superCategory, String name, Optional<String> remark, Sudoers sudoers) {
		Preconditions.checkNotNull(superCategory, "superCategory can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");

		this.superCategory = superCategory;
		this.name = name;
		this.remark = remark.orNull();
		this.createdMeta.setUpdated(sudoers);
		this.dateMeta.setUpdated(DateUtils.currentUTC());
	}

	public void delete(Sudoers sudoers) {
		this.deleted = true;
		this.createdMeta.setUpdated(sudoers);
		this.dateMeta.setUpdated(DateUtils.currentUTC());
	}

	public String getId() {
		return id;
	}

	public boolean isDeleted() {
		return deleted;
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

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public CreatedMeta getCreatedMeta() {
		return createdMeta;
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

		if (this.getClass() == obj.getClass()) {
			return true;
		}
		final ProductCategory categoryProduct = (ProductCategory) obj;
		return categoryProduct.id == this.id;
	}

	public ProductCategoryShell toShell() {
		return new ProductCategoryShell(this.superCategory, this.id, this.name, Optional.fromNullable(this.remark),
				dateMeta.toShell(), createdMeta.toShell(), this.deleted);
	}

}
