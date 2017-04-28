package com.chillies.smartshopper.common.shell.web_admin;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * ProductShell : is Shell class. which will be send to Launcher or Web.
 * 
 * NOTE : If any where null value is returned that is used cautiously.
 * 
 * 
 * @author Jinen Kothari
 *
 */
public class ProductShell {

	private final String id;

	private final String name;

	private final String remark;

	private final DateMetaShell dateMeta;

	private final CreatedMetaShell createdMeta;

	private final double price;

	private final double points;

	private final String path;

	private final String productURL;

	private final ProductCategoryShell category;

	public ProductShell(final String id, final String name, final Optional<String> remark, final DateMetaShell dateMeta,
			final CreatedMetaShell createdMeta, final String price, final String points, final Optional<String> path,
			final Optional<String> productURL, final ProductCategoryShell categoryShell) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");
		Preconditions.checkNotNull(price, "price can not be null.");
		Preconditions.checkNotNull(points, "points can not be null.");
		Preconditions.checkNotNull(path, "path can not be null.");
		Preconditions.checkNotNull(productURL, "productURL can not be null.");
		Preconditions.checkNotNull(categoryShell, "categoryShell can not be null.");
		this.id = id;
		this.name = name;
		this.remark = remark.orNull();
		this.dateMeta = dateMeta;
		this.createdMeta = createdMeta;
		this.price = AppUtils.stringToDouble(price);
		this.points = AppUtils.stringToDouble(points);
		this.path = path.orNull();
		this.productURL = productURL.orNull();
		this.category = categoryShell;
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

	public double getPrice() {
		return price;
	}

	public double getPoints() {
		return points;
	}

	public String getPath() {
		return path;
	}

	public String getProductURL() {
		return productURL;
	}

	public ProductCategoryShell getCategory() {
		return category;
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
		final ProductShell product = (ProductShell) obj;
		return product.id == this.id;
	}

}
