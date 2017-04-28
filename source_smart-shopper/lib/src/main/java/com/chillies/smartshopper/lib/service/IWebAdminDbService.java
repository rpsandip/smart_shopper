package com.chillies.smartshopper.lib.service;

import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.google.common.base.Optional;

/**
 * 
 * IWebAdminDbService : is DbService for fetching data from database.
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Service
@Scope("singleton")
public interface IWebAdminDbService {

	// admin users
	public Sudoers save(final Sudoers sudoers) throws DbException;

	public Sudoers byUsername(final String username) throws DbException;

	// product category
	public ProductCategory save(final ProductCategory productCategory) throws DbException;

	public Optional<ProductCategory> byProductCategoryById(final String id) throws DbException;

	public Optional<ProductCategory> byProductCategory(final String name) throws DbException;

	public Set<ProductCategory> productCategories() throws DbException;

	// product
	public Product save(final Product product) throws DbException;

	public Optional<Product> byProductId(final String id) throws DbException;

	public Optional<Product> byProduct(final String name) throws DbException;

	public Set<Product> products() throws DbException;

	public Set<Product> products(final ProductCategory category) throws DbException;
}
