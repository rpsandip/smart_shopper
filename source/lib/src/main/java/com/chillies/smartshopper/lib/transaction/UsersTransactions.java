package com.chillies.smartshopper.lib.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.model.ContactMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.Users;
import com.chillies.smartshopper.lib.service.IDbService;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * SudoersTransaction : is for database transaction for @Sudoers
 * 
 * @author Jinen Kothari
 *
 */
@Service
public final class UsersTransactions {

	private static final Logger LOG = LoggerFactory.getLogger(UsersTransactions.class);

	@Autowired
	private IDbService dbService;

	public Optional<Users> userByUsername(final String username) {
		Preconditions.checkNotNull(username, "username can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.usersByUsername(username));
		return opUsers;
	}

	public Optional<Users> userByPhoneNo(final String phoneNo) {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.userByPhoneNo(phoneNo));
		return opUsers;
	}

	public boolean isUsername(final String username) {
		Preconditions.checkNotNull(username, "username can not be null.");
		if (this.userByUsername(username).isPresent()) {
			throw new NotAccatable(MessageUtils.USERNAME_ALREADY_EXISTS);
		}
		return true;
	}

	public boolean isPhoneNo(final String phoneNo) {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		if (this.userByPhoneNo(phoneNo).isPresent()) {
			throw new NotAccatable(MessageUtils.PHONE_NO_ASSOCIATED_WITH_OTHER_USERNAME);
		}
		return true;
	}

	public Optional<Users> userByReferralCode(final String referralCode) {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.userByReferralCode(referralCode));
		return opUsers;
	}

	public Users save(final String username, final String password, final String firstName, final String lastName,
			final Optional<String> remark, final Optional<String> street, final Optional<String> city,
			final Optional<String> state, final Optional<String> country, final Optional<String> postalCode,
			final String phoneNo) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(password, "password can not be null.");
		Preconditions.checkNotNull(firstName, "firstName can not be null.");
		Preconditions.checkNotNull(lastName, "lastName can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(street, "street can not be null.");
		Preconditions.checkNotNull(city, "city can not be null.");
		Preconditions.checkNotNull(state, "state can not be null.");
		Preconditions.checkNotNull(country, "country can not be null.");
		Preconditions.checkNotNull(postalCode, "postalCode can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");

		final ContactMeta contactMeta = new ContactMeta(street, city, state, country, postalCode, phoneNo);
		final String referralCode = AppUtils.getNewReferralCode();

		if (this.userByReferralCode(referralCode).isPresent()) {
			LOG.info(String.format("save() Failed genrated referra-code:%s is duplicated.", referralCode));
		}

		final Users users = new Users(username, password, firstName, lastName, new DateMeta(Optional.absent()),
				referralCode, Optional.fromNullable(contactMeta), remark);
		return dbService.save(users);
	}
}
