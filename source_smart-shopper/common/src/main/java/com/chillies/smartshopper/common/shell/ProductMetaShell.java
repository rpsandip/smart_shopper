package com.chillies.smartshopper.common.shell;

import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.google.common.base.Preconditions;

public class ProductMetaShell {

	private ProductShell product;

	private int quantity;

	private double price;

	private double discount;

	public ProductMetaShell(final ProductShell product, int quantity, final String price, final String discount) {
		Preconditions.checkNotNull(product, "product can not be null.");
		Preconditions.checkNotNull(price, "price can not be null.");
		Preconditions.checkNotNull(discount, "discount can not be null.");
		this.product = product;
		this.quantity = quantity;
		this.price = AppUtils.stringToDouble(price);
		this.discount = AppUtils.stringToDouble(discount);
	}

	public ProductShell getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}

	public double getDiscount() {
		return discount;
	}

	@Override
	public int hashCode() {
		final int hash = this.product.getId().hashCode();
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
		final ProductMetaShell product = (ProductMetaShell) obj;
		if (product.product.getId() == this.product.getId() && product.getPrice() == this.getPrice()
				&& product.getQuantity() == this.getQuantity() && product.getDiscount() == this.getDiscount()) {
			return true;
		} else {
			return false;
		}
	}

}
