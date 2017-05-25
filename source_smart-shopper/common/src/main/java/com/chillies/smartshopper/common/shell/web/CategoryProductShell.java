package com.chillies.smartshopper.common.shell.web;

import java.util.SortedSet;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.google.common.base.Preconditions;

public class CategoryProductShell {

	private final String id;

	private final String name;

	private final String remark;

	private final DateMetaShell dateMeta;

	private final CreatedMetaShell createdMeta;

	private final SortedSet<ProductShell> products;

	public CategoryProductShell(final ProductCategoryShell categoryShell, final SortedSet<ProductShell> products) {
		Preconditions.checkNotNull(categoryShell, "categoryShell can not be null.");
		this.id = categoryShell.getId();
		this.name = categoryShell.getName();
		this.remark = categoryShell.getRemark();
		this.dateMeta = categoryShell.getDateMeta();
		this.createdMeta = categoryShell.getCreatedMeta();
		this.products = products;
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

	public SortedSet<ProductShell> getProducts() {
		return products;
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
		final CategoryProductShell categoryProduct = (CategoryProductShell) obj;
		return categoryProduct.id == this.id;
	}

}
