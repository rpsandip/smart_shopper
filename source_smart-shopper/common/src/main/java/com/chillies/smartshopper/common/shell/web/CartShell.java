package com.chillies.smartshopper.common.shell.web;

import java.util.Set;

import com.chillies.smartshopper.common.shell.DateMetaShell;
import com.chillies.smartshopper.common.shell.ProductMetaShell;
import com.chillies.smartshopper.common.util.CartStatus;
import com.google.common.base.Preconditions;

public class CartShell {

	private String id;

	private DateMetaShell date;

	private CartStatus status;

	private UsersShell users;

	private Set<ProductMetaShell> products;

	public CartShell(final String id, final DateMetaShell date, final CartStatus status, final UsersShell users,
			final Set<ProductMetaShell> products) {
		Preconditions.checkNotNull(id, "id can not be null.");
		Preconditions.checkNotNull(date, "date can not be null.");
		Preconditions.checkNotNull(status, "status can not be null.");
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(products, "products can not be null.");
		this.id = id;
		this.date = date;
		this.status = status;
		this.users = users;
		this.products = products;
	}

	public String getId() {
		return id;
	}

	public DateMetaShell getDate() {
		return date;
	}

	public CartStatus getStatus() {
		return status;
	}

	public UsersShell getUsers() {
		return users;
	}

	public Set<ProductMetaShell> getProducts() {
		return products;
	}

}
