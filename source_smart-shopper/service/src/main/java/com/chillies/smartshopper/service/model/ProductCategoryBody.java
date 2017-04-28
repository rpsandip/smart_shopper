package com.chillies.smartshopper.service.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * ProductCategoryBody : is request body object that Application will consume.
 * 
 * Validation of field and If Date is there it will be taken as yyyyMMdd or
 * yyyyMMdd HH:mm:ss Z
 * 
 * @author Jinen Kothari
 *
 */
public class ProductCategoryBody {

	@NotBlank(message = "name can not be null.")
	private String name;

	private String remark;

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductCategoryBody [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (remark != null)
			builder.append("remark=").append(remark);
		builder.append("]");
		return builder.toString();
	}

}
