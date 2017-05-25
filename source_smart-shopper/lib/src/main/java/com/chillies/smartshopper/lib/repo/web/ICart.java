package com.chillies.smartshopper.lib.repo.web;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.common.util.CartStatus;
import com.chillies.smartshopper.lib.model.web.Cart;
import com.chillies.smartshopper.lib.model.web.Users;

/**
 * 
 * ICart : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface ICart extends MongoRepository<Cart, String> {

	public Cart findByUsers(final Users users);
	
	public Cart findByUsersAndStatusIs(final Users users,final CartStatus status);
}
	