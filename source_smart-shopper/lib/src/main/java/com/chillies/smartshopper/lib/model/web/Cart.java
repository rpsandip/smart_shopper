package com.chillies.smartshopper.lib.model.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chillies.smartshopper.common.shell.ProductMetaShell;
import com.chillies.smartshopper.common.shell.web.CartShell;
import com.chillies.smartshopper.common.util.CartStatus;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.ProductMeta;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Cart : MongoDb Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
@Document
public class Cart {

	@Id
	private String id;

	private DateMeta date;

	private CartStatus status;

	@DBRef
	private Users users;

	private Set<ProductMeta> products;

	/**
	 * Db use only.
	 * 
	 * @deprecated
	 */
	public Cart() {
	}

	public Cart(final DateMeta date, final CartStatus status, final Users users, final Set<ProductMeta> products) {
		Preconditions.checkNotNull(date, "date can not be null.");
		Preconditions.checkNotNull(status, "status can not be null.");
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(products, "products can not be null.");
		this.date = date;
		this.status = status;
		this.users = users;
		this.products = products;
	}

	public void checkedOutStatus() {
		this.status = CartStatus.CHECKED_OUT;
	}

	public void addProduct(final ProductMeta productMeta) {
		Preconditions.checkNotNull(productMeta, "productMeta can not be null.");

		final Set<ProductMeta> products = new HashSet<>();
		products.add(productMeta);
		if (this.products != null) {
			products.addAll(this.products);
		}
		this.products = products;
	}

	public void removeProductMeta(final ProductMeta productMeta) {
		Preconditions.checkNotNull(productMeta, "productMeta can not be null.");
		if (this.products != null) {
			this.products.remove(productMeta);
		}
	}

	public Optional<ProductMeta> getProducts(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");

		if (this.products == null) {
			return Optional.absent();
		}

		for (final ProductMeta product : this.products) {
			if (product.getProduct().getId().equals(id)) {
				return Optional.of(product);
			}
		}

		return Optional.absent();
	}

	public String getId() {
		return id;
	}

	public DateMeta getDate() {
		return date;
	}

	public CartStatus getStatus() {
		return status;
	}

	public Users getUsers() {
		return users;
	}

	public Set<ProductMeta> getProducts() {
		return products;
	}

	@Override
	public int hashCode() {
		final int hash = id.hashCode();
		return hash * 3;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		final Cart cart = (Cart) obj;
		return cart.id == this.id;
	}

	public CartShell toShell(final String baseURL) {
		final Set<ProductMetaShell> productMetaShells = new HashSet<>();

		this.products.forEach(p -> productMetaShells.add(p.toShell(baseURL)));
		return new CartShell(this.id, this.date.toShell(), this.status, this.users.toShell(), productMetaShells);
	}
}
