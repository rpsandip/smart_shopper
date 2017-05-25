package com.chillies.smartshopper.lib.repo.web;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web.Order;
import com.chillies.smartshopper.lib.model.web.Users;

/**
 * 
 * IOrder : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface IOrder extends MongoRepository<Order, String> {

	public List<Order> findByUsers(Users users);
}
