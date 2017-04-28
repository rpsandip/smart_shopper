package com.chillies.smartshopper.lib.repo.web_admin;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;

/**
 * 
 * IProduct : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface IProduct extends MongoRepository<Product, String> {

	public Product findByName(final String name);

	public List<Product> findByCategory(final ProductCategory category);
}
