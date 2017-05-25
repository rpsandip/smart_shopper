package com.chillies.smartshopper.lib.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.CartStatus;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.web.Cart;
import com.chillies.smartshopper.lib.model.web.Order;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.lib.repo.web.ICart;
import com.chillies.smartshopper.lib.repo.web.IOrder;
import com.chillies.smartshopper.lib.repo.web.IUsers;
import com.chillies.smartshopper.lib.service.IWebDbService;
import com.google.common.base.Preconditions;

@Service
public final class WebDbServiceImpl implements IWebDbService {

	@Autowired
	private IUsers iUsers;

	@Autowired
	private ICart iCart;

	@Autowired
	private IOrder iOrder;

	@Override
	public Users save(final Users users) throws DbException {
		Preconditions.checkNotNull(users, "users can not be null.");
		return iUsers.save(users);
	}

	@Override
	public Users byUserId(final String id) throws DbException {
		Preconditions.checkNotNull(id, "id can not be null");
		return iUsers.findOne(id);
	}

	@Override
	public Users byUsername(final String username) throws DbException {
		Preconditions.checkNotNull(username, "username can not be null.");
		return iUsers.findByUsername(username);
	}

	@Override
	public Users byPhoneNo(final String phoneNo) throws DbException {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		return iUsers.findByContactMetaPhoneNo(phoneNo);
	}

	@Override
	public Users byReferralCode(final String referralCode) throws DbException {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");
		return iUsers.findByReferralCode(referralCode);
	}

	@Override
	public Set<Users> all() throws DbException {
		final List<Users> list = iUsers.findAll();
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Users bySubUsers(final Users users) throws DbException {
		Preconditions.checkNotNull(users, "users can not be null.");
		return iUsers.findBySubUsersIn(users);
	}

	@Override
	public Cart save(final Cart cart) throws DbException {
		Preconditions.checkNotNull(cart, "cart can not be null.");
		return iCart.save(cart);
	}

	@Override
	public Cart byUsername(final Users users) throws DbException {
		Preconditions.checkNotNull(users, "users can not be null.");
		return iCart.findByUsers(users);
	}

	@Override
	public Cart byUsername(final Users users, final CartStatus cartStatus) throws DbException {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(cartStatus, "cartStatus can not be null.");

		return iCart.findByUsersAndStatusIs(users, cartStatus);
	}

	@Override
	public Order save(final Order order) throws DbException {
		Preconditions.checkNotNull(order, "order can not be null.");
		return iOrder.save(order);
	}

	@Override
	public Set<Order> orders(final Users users) throws DbException {
		final List<Order> list = iOrder.findByUsers(users);
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Set<Order> orders() throws DbException {
		final List<Order> list = iOrder.findAll();
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public Order byId(final String id) throws DbException {
		Preconditions.checkNotNull(id, "id can not be null.");
		return iOrder.findOne(id);
	}

}
