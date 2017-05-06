package com.chillies.smartshopper.lib.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.chillies.smartshopper.common.shell.ProductMetaShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.google.common.base.Preconditions;

public class ProductMeta {

	@DBRef
	private Product product;

	private int quantity;

	private String price;

	private String discount;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public ProductMeta() {
	}

	public ProductMeta(final Product product, int quantity, final double price, final double discount) {
		Preconditions.checkNotNull(product, "product can not be null.");
		this.product = product;
		this.quantity = quantity;
		this.price = AppUtils.doubleToString(price);
		this.discount = AppUtils.doubleToString(discount);
	}

	public void update(int quantity, double price, double discount) {
		this.quantity = quantity;
		this.price = AppUtils.doubleToString(price);
		this.discount = AppUtils.doubleToString(discount);
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getPrice() {
		return price;
	}

	public double price() {
		return AppUtils.stringToDouble(this.price);
	}

	public String getDiscount() {
		return discount;
	}

	public ProductMetaShell toShell(final String baseURL) {
		return new ProductMetaShell(this.product.toShell(baseURL), this.quantity, this.price, this.discount);
	}
}
