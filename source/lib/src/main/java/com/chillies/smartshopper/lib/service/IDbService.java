package com.chillies.smartshopper.lib.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.model.Sudoers;
import com.chillies.smartshopper.lib.model.Users;

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
public interface IDbService {

	// admin users
	public Sudoers save(final Sudoers sudoers) throws DbException;

	public Sudoers byUsername(final String username) throws DbException;

	// web users
	public Users save(final Users users) throws DbException;

	public Users usersByUsername(final String username) throws DbException;

	public Users userByPhoneNo(final String phoneNo) throws DbException;

	public Users userByReferralCode(final String referralCode) throws DbException;

}
