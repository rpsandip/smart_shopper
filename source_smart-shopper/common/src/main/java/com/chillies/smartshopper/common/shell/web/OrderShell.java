package com.chillies.smartshopper.common.shell.web;

import com.chillies.smartshopper.common.shell.CreatedMetaShell;
import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.OrderStatus;
import com.google.common.base.Preconditions;

public class OrderShell {

	private final String id;

	private final DateMetaShell date;

	private final UsersShell users;

	private final OrderStatus status;

	private final String orderStatus;

	private final double total;

	private final double discountAmount;

	private final double discount;

	private final CreatedMetaShell createdMeta;

	private final CartShell cart;

	public OrderShell(String id, DateMetaShell date, UsersShell users, OrderStatus status, String total,
			String discountAmount, String discount, CreatedMetaShell createdMeta, CartShell cart) {
		Preconditions.checkNotNull(id, "id can not be null");
		Preconditions.checkNotNull(date, "date can not be null");
		Preconditions.checkNotNull(users, "users can not be null");
		Preconditions.checkNotNull(status, "status can not be null");
		Preconditions.checkNotNull(total, "total can not be null");
		Preconditions.checkNotNull(discountAmount, "discountAmount can not be null");
		Preconditions.checkNotNull(discount, "discount can not be null");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null");
		Preconditions.checkNotNull(cart, "cart can not be null");
		this.id = id;
		this.date = date;
		this.users = users;
		this.status = status;
		this.orderStatus = status.getName();
		this.total = AppUtils.stringToDouble(total);
		this.discountAmount = AppUtils.stringToDouble(discountAmount);
		this.discount = AppUtils.stringToDouble(discount);
		this.createdMeta = createdMeta;
		this.cart = cart;
	}

	public String getId() {
		return id;
	}

	public DateMetaShell getDate() {
		return date;
	}

	public UsersShell getUsers() {
		return users;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public double getTotal() {
		return total;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public CreatedMetaShell getCreatedMeta() {
		return createdMeta;
	}

	public CartShell getCart() {
		return cart;
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 4;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		final OrderShell order = (OrderShell) obj;
		return order.id == this.id;
	}

}
