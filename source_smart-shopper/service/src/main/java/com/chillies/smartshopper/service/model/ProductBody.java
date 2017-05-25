package com.chillies.smartshopper.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * ProductBody : is request body object that Application will consume.
 * 
 * Validation of field and If Date is there it will be taken as yyyyMMdd or
 * yyyyMMdd HH:mm:ss Z
 * 
 * @author Jinen Kothari
 *
 */
public class ProductBody {

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("remark")
	@Expose
	private String remark;

	@SerializedName("price")
	@Expose
	private String price;

	@SerializedName("points")
	@Expose
	private String points;

	@SerializedName("categoryId")
	@Expose
	private String categoryId;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public String getPrice() {
		return price;
	}

	public String getPoints() {
		return points;
	}

	public String getCategoryId() {
		return categoryId;
	}

}
