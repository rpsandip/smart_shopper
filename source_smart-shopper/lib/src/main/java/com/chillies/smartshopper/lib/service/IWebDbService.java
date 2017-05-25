package com.chillies.smartshopper.lib.service;

import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.CartStatus;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.web.Cart;
import com.chillies.smartshopper.lib.model.web.Order;
import com.chillies.smartshopper.lib.model.web.Users;

/**
 * 
 * IDbService : is DbService for fetching data from database.
 * 
 * NOTE : singleton interface.
 * 
 * @author Jinen Kothari
 *
 */
@Service
@Scope("singleton")
public interface IWebDbService {

	// web users
	public Users save(final Users users) throws DbException;

	public Users byUsername(final String username) throws DbException;

	public Users byPhoneNo(final String phoneNo) throws DbException;

	public Users byReferralCode(final String referralCode) throws DbException;

	public Users byUserId(final String id) throws DbException;

	public Set<Users> all() throws DbException;

	public Users bySubUsers(final Users users) throws DbException;

	// cart
	public Cart save(final Cart cart) throws DbException;

	public Cart byUsername(final Users users) throws DbException;

	public Cart byUsername(final Users users, final CartStatus cartStatus) throws DbException;

	// Order
	public Order save(final Order order) throws DbException;

	public Set<Order> orders(final Users users) throws DbException;

	public Set<Order> orders() throws DbException;

	public Order byId(final String id) throws DbException;
}
