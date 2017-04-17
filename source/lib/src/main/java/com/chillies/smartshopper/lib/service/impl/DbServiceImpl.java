package com.chillies.smartshopper.lib.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.Sudoers;
import com.chillies.smartshopper.lib.model.Users;
import com.chillies.smartshopper.lib.repo.ISudoers;
import com.chillies.smartshopper.lib.repo.IUsers;
import com.chillies.smartshopper.lib.service.IDbService;
import com.google.common.base.Preconditions;

@Service
public final class DbServiceImpl implements IDbService {

	@Autowired
	private ISudoers iSudoers;

	@Autowired
	private IUsers iUsers;

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
	public Users save(final Users users) throws DbException {
		Preconditions.checkNotNull(users, "users can not be null.");
		return iUsers.save(users);
	}

	@Override
	public Users usersByUsername(final String username) throws DbException {
		Preconditions.checkNotNull(username, "username can not be null.");
		return iUsers.findByUsername(username);
	}

	@Override
	public Users userByPhoneNo(final String phoneNo) throws DbException {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		return iUsers.findByContactMetaPhoneNo(phoneNo);
	}

	@Override
	public Users userByReferralCode(final String referralCode) throws DbException {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");
		return iUsers.findByReferralCode(referralCode);
	}

}
