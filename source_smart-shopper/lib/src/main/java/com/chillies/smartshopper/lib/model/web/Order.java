package com.chillies.smartshopper.lib.model.web;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.web.OrderShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.common.util.OrderStatus;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.google.common.base.Preconditions;

/**
 * Order : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class Order {

	@Id
	private String id;

	private DateMeta date;

	@DBRef
	private Users users;

	private OrderStatus status;

	private String total;

	private String discountAmount;

	private String discount;

	private CreatedMeta createdMeta;

	@DBRef
	private Cart cart;

	/**
	 * Db Use only.
	 * 
	 * @deprecated
	 */
	public Order() {
	}

	public Order(DateMeta date, Users users, OrderStatus status, double total, double discountAmount, double discount,
			CreatedMeta createdMeta, Cart cart) {
		Preconditions.checkNotNull(date, "date can not be null");
		Preconditions.checkNotNull(users, "users can not be null");
		Preconditions.checkNotNull(status, "status can not be null");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null");
		Preconditions.checkNotNull(cart, "cart can not be null");
		this.date = date;
		this.users = users;
		this.status = status;
		this.total = AppUtils.doubleToString(total);
		this.discountAmount = AppUtils.doubleToString(discountAmount);
		this.discount = AppUtils.doubleToString(discount);
		this.createdMeta = createdMeta;
		this.cart = cart;
	}

	public void cancel() {
		this.status = OrderStatus.CANCEL;
		this.date.setUpdated(DateUtils.currentUTC());
	}

	public void status(OrderStatus status, Sudoers sudoers) {
		Preconditions.checkNotNull(status, "status can not be null");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null");
		this.status = status;
		this.createdMeta.setUpdated(sudoers);
	}

	public String getId() {
		return id;
	}

	public DateMeta getDate() {
		return date;
	}

	public Users getUsers() {
		return users;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public String getTotal() {
		return total;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public String getDiscount() {
		return discount;
	}

	public CreatedMeta getCreatedMeta() {
		return createdMeta;
	}

	public Cart getCart() {
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
		final Order order = (Order) obj;
		return order.id == this.id;
	}

	public OrderShell toShell(final String baseURL) {
		return new OrderShell(this.id, this.date.toShell(), this.users.toShell(), this.status, this.total,
				this.discountAmount, this.discount, this.createdMeta.toShell(), this.cart.toShell(baseURL));
	}

}
