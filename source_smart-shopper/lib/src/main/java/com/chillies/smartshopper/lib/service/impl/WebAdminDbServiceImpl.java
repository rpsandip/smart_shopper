package com.chillies.smartshopper.lib.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.repo.web_admin.IProduct;
import com.chillies.smartshopper.lib.repo.web_admin.IProductCataegory;
import com.chillies.smartshopper.lib.repo.web_admin.ISudoers;
import com.chillies.smartshopper.lib.service.IWebAdminDbService;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

@Service
public final class WebAdminDbServiceImpl implements IWebAdminDbService {

	@Autowired
	private ISudoers iSudoers;

	@Autowired
	private IProductCataegory iProductCataegory;

	@Autowired
	private IProduct iProduct;

	@Override
	public Sudoers save(final Sudoers sudoers) throws DbException {
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		return iSudoers.save(sudoers);
	}

	@Override
	public Sudoers byUsername(final String username) throws DbException {
		Preconditions.checkNotNull(username, "username can not be null.");
		return iSudoers.findByUsername(username);
	}

	@Override
	public ProductCategory save(final ProductCategory productCategory) throws DbException {
		Preconditions.checkNotNull(productCategory, "productCategory can not be null.");
		return iProductCataegory.save(productCategory);
	}

	@Override
	public Optional<ProductCategory> byProductCategoryById(final String id) throws DbException {
		Preconditions.checkNotNull(id, "id can not be null.");
		return Optional.fromNullable(iProductCataegory.findOne(id));
	}

	@Override
	public Optional<ProductCategory> byProductCategory(String name) throws DbException {
		Preconditions.checkNotNull(name, "name can not be null.");
		return Optional.fromNullable(iProductCataegory.findByName(name));
	}

	@Override
	public Set<ProductCategory> productCategories() throws DbException {
		final List<ProductCategory> list = iProductCataegory.findAll();
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Product save(final Product product) throws DbException {
		Preconditions.checkNotNull(product, "product can not be null.");
		return iProduct.save(product);
	}

	@Override
	public Optional<Product> byProductId(final String id) throws DbException {
		Preconditions.checkNotNull(id, "id can not be null.");
		return Optional.fromNullable(iProduct.findOne(id));
	}

	@Override
	public Set<Product> products() throws DbException {
		final List<Product> list = iProduct.findAll();
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Set<Product> products(final ProductCategory category) throws DbException {
		Preconditions.checkNotNull(category, "category can not be null.");
		final List<Product> list = iProduct.findByCategory(category);
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Optional<Product> byProduct(final String name) throws DbException {
		Preconditions.checkNotNull(name, "name can not be null.");
		return Optional.fromNullable(iProduct.findByName(name));
	}
}
