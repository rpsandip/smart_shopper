package com.chillies.smartshopper.lib.transaction;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.model.ContactMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.lib.service.IWebDbService;
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
	private IWebDbService dbService;

	private Users save(final Users user) {
		Preconditions.checkNotNull(user, "user can not be null.");

		return dbService.save(user);
	}

	private Optional<Users> byUsername(final String username) {
		Preconditions.checkNotNull(username, "username can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.byUsername(username));
		return opUsers;
	}

	private Optional<Users> byPhoneNo(final String phoneNo) {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.byPhoneNo(phoneNo));
		return opUsers;
	}

	private Optional<Users> byReferralCode(final String referralCode) {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");

		final Optional<Users> opUsers = Optional.fromNullable(dbService.byReferralCode(referralCode));
		return opUsers;
	}

	private Optional<Users> byUserId(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");
		final Optional<Users> optionalUsers = Optional.fromNullable(dbService.byUserId(id));
		return optionalUsers;
	}

	protected Optional<Users> bySubUser(final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");

		return Optional.fromNullable(dbService.bySubUsers(users));
	}

	private Users apply3Percent(final double total, final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");
		final Users firstParent = this.addPoints(users, this.getPercentageAmount(total, 3));
		

		final Optional<Users> optional = this.bySubUser(firstParent);
		if (!optional.isPresent()) {
			return firstParent;
		}
		return this.apply6Percent(total, optional.get());
	}

	private Users apply6Percent(final double total, final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");
		final Users secondParent = this.addPoints(users, this.getPercentageAmount(total, 6));

		final Optional<Users> optional = this.bySubUser(secondParent);
		if (!optional.isPresent()) {
			return secondParent;
		}
		return this.apply9Percent(total, optional.get());
	}

	private Users apply9Percent(final double total, final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");
		final Users thirdParent = this.addPoints(users, this.getPercentageAmount(total, 9));

		final Optional<Users> optional = this.bySubUser(thirdParent);
		if (!optional.isPresent()) {
			return thirdParent;
		}
		return this.apply12Percent(total, optional.get());
	}

	private Users apply12Percent(final double total, final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");
		final Users fourthParent = this.addPoints(users, this.getPercentageAmount(total, 12));

		final Optional<Users> optional = this.bySubUser(fourthParent);
		if (!optional.isPresent()) {
			return fourthParent;
		}
		return this.apply12Percent(total, optional.get());
	}

	private double getPercentageAmount(final double total, final double percentage) {
		return (total * percentage) / 100;
	}

	public boolean isUsername(final String username) {
		Preconditions.checkNotNull(username, "username can not be null.");
		if (this.byUsername(username).isPresent()) {
			throw new NotAccatable(MessageUtils.USERNAME_ALREADY_EXISTS);
		}
		return true;
	}

	public boolean isPhoneNo(final String phoneNo) {
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		if (this.byPhoneNo(phoneNo).isPresent()) {
			throw new NotAccatable(MessageUtils.PHONE_NO_ASSOCIATED_WITH_OTHER_USERNAME);
		}
		return true;
	}

	public Set<Users> users() {
		return dbService.all();
	}

	public Users getUsersByUsername(final String username) {
		Preconditions.checkNotNull(username, "username can not be null.");

		final Optional<Users> optionalUsers = this.byUsername(username);

		if (!optionalUsers.isPresent()) {
			final String error = MessageUtils.userNotRegistered(username);
			LOG.info(String.format("getUsersByUsername() %s is not register.", username));
			throw new NotAccatable(error);
		}
		return optionalUsers.get();
	}

	public Users update(final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");

		return dbService.save(users);
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

		if (this.byReferralCode(referralCode).isPresent()) {
			LOG.info(String.format("save() Failed genrated referra-code:%s is duplicated.", referralCode));
		}

		final Users users = new Users(username, password, firstName, lastName, new DateMeta(Optional.absent()),
				referralCode, Optional.fromNullable(contactMeta), remark, Optional.absent());
		return dbService.save(users);
	}

	public Users addSubUsers(final Users parent, final Users child) {
		Preconditions.checkNotNull(parent, "parent can not be null.");
		Preconditions.checkNotNull(child, "child can not be null.");

		parent.addSubUsers(child);
		return dbService.save(parent);
	}

	public Users getUserByReferralCode(final String referralCode) {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");

		final Optional<Users> optionalUsers = this.byReferralCode(referralCode);

		if (!optionalUsers.isPresent()) {
			final String error = MessageUtils.referralCodeNotPresent(referralCode);
			LOG.info(String.format("getUserByReferralCode() %s is not associated with any user.", referralCode));
			throw new NotAccatable(error);
		}
		return optionalUsers.get();
	}

	public Users getUserById(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");

		final Optional<Users> optionalUsers = this.byUserId(id);
		if (!optionalUsers.isPresent()) {
			throw new NotAccatable(MessageUtils.USER_NOT_REGISTER);
		}
		return optionalUsers.get();
	}

	public SortedSet<UsersShell> getSortedUsers() {

		final SortedSet<UsersShell> usersShells = new TreeSet<>(Comparator.comparing(UsersShell::getId))
				.descendingSet();
		final Set<Users> users = this.users();
		if (users != null) {
			users.forEach(user -> usersShells.add(user.toShell()));
		}
		return usersShells;
	}

	public Users addPoints(final Users users, final double points) {
		Preconditions.checkNotNull(users, "users can not be null.");
		users.addPoints(points);
		return this.save(users);
	}

	public Optional<Users> addPointsToParent(final Users users, final double totalAmount) {
		Preconditions.checkNotNull(users, "users can not be null.");
		
		return Optional.fromNullable(this.apply3Percent(totalAmount, users));
	}
}
