package com.chillies.smartshopper.common.util;

import com.google.common.base.Preconditions;

public enum OrderStatus {

	PLACESED("Placed"), OUT_FOR_DELIVERY("Out for delivery"), DELIVERED("Delivered"), CANCEL("Cancel");

	private final String name;

	private OrderStatus(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
