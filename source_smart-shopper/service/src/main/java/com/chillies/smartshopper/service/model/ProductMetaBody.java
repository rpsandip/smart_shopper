package com.chillies.smartshopper.service.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

/**
 * ProdctMetaBody : is request body object that Application will consume.
 * 
 * Validation of field and If Date is there it will be taken as yyyyMMdd or
 * yyyyMMdd HH:mm:ss Z
 * 
 * @author Jinen Kothari
 *
 */
public class ProductMetaBody {

	@NotBlank(message = "productId can not be null.")
	private String productId;

	@Min(-2)
	private int quantity;

	public String getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "ProdctMetaBody [" + (productId != null ? "productId=" + productId + ", " : "") + "quantity=" + quantity
				+ "]";
	}

}
