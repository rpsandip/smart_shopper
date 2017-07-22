package com.chillies.smartshopper.service.model;

import org.hibernate.validator.constraints.NotBlank;

import com.chillies.smartshopper.common.util.SuperCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

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

	private String id;

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	private SuperCategory superCategory;

	@NotBlank(message = "name can not be null.")
	private String name;

	private String remark;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductCategoryBody [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (superCategory != null)
			builder.append("superCategory=").append(superCategory).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (remark != null)
			builder.append("remark=").append(remark);
		builder.append("]");
		return builder.toString();
	}

}
