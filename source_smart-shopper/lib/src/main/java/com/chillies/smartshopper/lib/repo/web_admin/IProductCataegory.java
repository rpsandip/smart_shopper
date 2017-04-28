package com.chillies.smartshopper.lib.repo.web_admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web_model.ProductCategory;

/**
 * 
 * IProductCataegory : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface IProductCataegory extends MongoRepository<ProductCategory, String> {

	public ProductCategory findByName(final String name);
}
