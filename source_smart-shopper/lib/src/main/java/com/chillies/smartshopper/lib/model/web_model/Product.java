package com.chillies.smartshopper.lib.model.web_model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Product : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class Product {

	@Id
	private String id;

	@Indexed(unique = true)
	@TextIndexed
	private String name;

	private String remark;

	private DateMeta dateMeta;

	private CreatedMeta createdMeta;

	private String price;

	private String points;

	private String path;

	@DBRef
	private ProductCategory category;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public Product() {
	}

	public Product(String name, Optional<String> remark, DateMeta dateMeta, CreatedMeta createdMeta, Double price,
			Double points, ProductCategory category, Optional<String> path) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "createdMeta can not be null.");
		Preconditions.checkNotNull(price, "price can not be null.");
		Preconditions.checkNotNull(points, "points can not be null.");
		Preconditions.checkNotNull(category, "category can not be null.");
		Preconditions.checkNotNull(path, "path can not be null.");

		this.name = name;
		this.remark = remark.orNull();
		this.dateMeta = dateMeta;
		this.createdMeta = createdMeta;
		this.price = AppUtils.doubleToString(price);
		this.points = AppUtils.doubleToString(points);
		this.category = category;
		this.path = path.orNull();
	}

	public void update(String name, Optional<String> remark, double price, double points, ProductCategory category,
			Sudoers sudoers) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(price, "price can not be null.");
		Preconditions.checkNotNull(points, "points can not be null.");
		Preconditions.checkNotNull(category, "category can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		this.name = name;
		this.remark = remark.orNull();
		this.price = AppUtils.doubleToString(price);
		this.points = AppUtils.doubleToString(points);
		this.category = category;
		this.createdMeta.setUpdated(sudoers);
		this.dateMeta.setUpdated(DateUtils.currentUTC());
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

	public DateMeta getDateMeta() {
		return dateMeta;
	}

	public CreatedMeta getCreatedMeta() {
		return createdMeta;
	}

	public String getPrice() {
		return price;
	}

	public double price() {
		return AppUtils.stringToDouble(price);
	}

	public String getPoints() {
		return points;
	}

	public double points() {
		return AppUtils.stringToDouble(points);
	}

	public ProductCategory getCategory() {
		return category;
	}

	public String getPath() {
		return path;
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
		final Product product = (Product) obj;
		return product.id == this.id;
	}

	public ProductShell toShell(final String baseURL) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be null.");

		final Optional<String> url = this.path != null
				? Optional.fromNullable(baseURL + AppUtils.URL_SEPRATOR + this.path) : Optional.absent();
		return new ProductShell(this.id, this.name, Optional.fromNullable(this.remark), this.dateMeta.toShell(),
				this.createdMeta.toShell(), this.price, this.points, Optional.fromNullable(this.path), url,
				this.category.toShell());
	}

}
