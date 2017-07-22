package com.chillies.smartshopper.service.dto;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.shell.PreferenceTncShell;
import com.chillies.smartshopper.common.shell.web.UsersAuthShell;
import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.chillies.smartshopper.common.util.EmailValidator;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.exception.ServicesUnauthorized;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.lib.model.web_model.PreferenceTnc;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.service.IMailService;
import com.chillies.smartshopper.service.manager.SessionManager;
import com.chillies.smartshopper.service.model.LoginBody;
import com.chillies.smartshopper.service.model.RegisterBody;
import com.chillies.smartshopper.service.transaction.SudoersTransaction;
import com.chillies.smartshopper.service.transaction.UsersTransactions;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * UsersDTO : is for converts body-object to entity-object, body-object
 * validation and entity-object to shell-objects.
 * 
 * As well as all the business logic will be written in this class.
 * 
 * @author Jinen Kothari
 *
 */
@Service
public class UsersDTO {

	private static final Logger LOG = LoggerFactory.getLogger(UsersDTO.class);

	@Autowired
	private UsersTransactions usersTransactions;

	@Autowired
	private SudoersTransaction sudoersTransaction;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private IMailService iMailService;

	public Users isValid(final String session) {
		Preconditions.checkNotNull(session, "username can not be null.");

		try {
			LOG.info(String.format("isValid() request %s", session));
			final String username = sessionManager.getSessionUser(session);

			final Users users = usersTransactions.getUsersByUsername(username);
			LOG.info(String.format("isValid() response %s", users.toString()));
			return users;
		} catch (DbException e) {
			final String error = String.format("isValid() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("isValid() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> register(final RegisterBody registerBody, final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(registerBody, "registerBody can not be null.");
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");
		try {
			LOG.info(String.format("register() request %s", registerBody.toString()));

			if (registerBody.getPassword() == null || registerBody.getPassword().isEmpty()) {
				throw new NotAccatable(MessageUtils.REQUIRED_FIELD);
			}

			final String username = registerBody.getUsername();

			if (!emailValidator.validate(username)) {
				throw new NotAccatable(MessageUtils.EMAIL_NOT_VALID);
			}

			final String phoneNo = registerBody.getContactBody().getPhoneNo();

			usersTransactions.isUsername(username);
			usersTransactions.isPhoneNo(phoneNo);

			final Optional<Users> optionalParent;
			if (registerBody.getParentId() != null) {
				System.out.println(registerBody.getParentId());
				optionalParent = Optional
						.fromNullable(usersTransactions.getUserByReferralCode(registerBody.getParentId()));
			} else {
				optionalParent = Optional.absent();
			}

			final Users users = usersTransactions.save(username, registerBody.getPassword(),
					registerBody.getFirstName(), registerBody.getLastName(),
					Optional.fromNullable(registerBody.getRemark()),
					Optional.fromNullable(registerBody.getContactBody().getStreet()),
					Optional.fromNullable(registerBody.getContactBody().getCity()),
					Optional.fromNullable(registerBody.getContactBody().getState()),
					Optional.fromNullable(registerBody.getContactBody().getCountry()),
					Optional.fromNullable(registerBody.getContactBody().getPostalCode()), phoneNo);

			if (optionalParent.isPresent()) {
				usersTransactions.addSubUsers(optionalParent.get(), users);
			}

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<UsersShell>(users.toShell(),
					HttpStatus.OK);

			LOG.info(String.format("register() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("register() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("register() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	// user service
	public ResponseEntity<UsersAuthShell> auth(final LoginBody loginBody, final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(loginBody, "loginBody can not be null.");
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");

		try {
			LOG.info(String.format("auth() request %s", loginBody.toString()));

			final Users users = usersTransactions.getUsersByUsername(loginBody.getUsername());

			if (!users.getPassword().equals(loginBody.getPassword())) {
				throw new ServicesNotAcceptable(MessageUtils.USER_N_PASSWORD_INCORRECT);
			}

			httpRequest.getSession().invalidate();

			final String session = httpRequest.getSession(true).getId();
			final String user = users.getUsername();

			final String userSessionCheck = sessionManager.getKey(user);

			if (userSessionCheck != null) {
				sessionManager.remove(userSessionCheck);
			}

			sessionManager.add(session, user);
			final ResponseEntity<UsersAuthShell> responseEntity = new ResponseEntity<UsersAuthShell>(
					new UsersAuthShell(session, users.toShell()), HttpStatus.OK);

			LOG.info(String.format("auth() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("auth() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("auth() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersAuthShell> isActive(final String session) {
		Preconditions.checkNotNull(session, "session can not be null.");

		try {

			LOG.info(String.format("isActive() request %s", session));

			final String user = sessionManager.getSessionUser(session);
			final Users users = usersTransactions.getUsersByUsername(user);

			final ResponseEntity<UsersAuthShell> responseEntity = new ResponseEntity<UsersAuthShell>(
					new UsersAuthShell(session, users.toShell()), HttpStatus.OK);

			LOG.info(String.format("isActive() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("isActive() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("isActive() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public void logout(final String session) {
		Preconditions.checkNotNull(session, "session can not be null.");

		try {

			LOG.info(String.format("logout() request %s", session));
			final String user = sessionManager.getSessionUser(session);
			sessionManager.remove(session);
			LOG.info(String.format("logout() %s is loggedout.", user));
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("logout() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}

	}

	public ResponseEntity<UsersShell> referralCode(final String referralCode) {
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");

		try {
			LOG.info(String.format("referralCode() request %s", referralCode));

			final Users users = usersTransactions.getUserByReferralCode(referralCode);

			if (!users.getActivateMeta().isActivate()) {
				throw new NotAccatable(MessageUtils.USER_IS_IN_ACTIVE);
			}

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<UsersShell>(users.toShell(),
					HttpStatus.OK);

			LOG.info(String.format("referralCode() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("referralCode() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("referralCode() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<UsersShell>> all() {
		try {
			final SortedSet<UsersShell> sortedUsers = usersTransactions.getSortedUsers();

			final ResponseEntity<SortedSet<UsersShell>> responseEntity = new ResponseEntity<>(sortedUsers,
					HttpStatus.OK);

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("all() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("all() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> activate(final String userId, final Sudoers sudoers) {
		Preconditions.checkNotNull(userId, "userId can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		try {

			LOG.info(String.format("activate() request %s", userId));
			final Users users = usersTransactions.activateUser(usersTransactions.getUserById(userId), sudoers);

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<>(users.toShell(), HttpStatus.OK);

			LOG.info("activate() response %s", responseEntity.toString());

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("activate() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("activate() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> getProfile(final String session) {
		Preconditions.checkNotNull(session, "session can not be null.");
		LOG.info("getProfile() request {}", session);
		try {
			final String user = sessionManager.getSessionUser(session);
			final Users users = usersTransactions.getUsersByUsername(user);

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<>(users.toShell(), HttpStatus.OK);

			LOG.info("getProfile() response {}", responseEntity.toString());

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("getProfile() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("getProfile() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> updateProfile(final String session, final RegisterBody registerBody) {
		Preconditions.checkNotNull(session, "session can not be null.");
		Preconditions.checkNotNull(registerBody, "registerBody can not be null.");
		LOG.info("updateProfile() request {} and {}", session, registerBody.toString());
		try {

			if (registerBody.getId() == null || registerBody.getId().isEmpty()) {
				throw new ServicesNotAcceptable(MessageUtils.REQUIRED_FIELD);
			}

			final String user = sessionManager.getSessionUser(session);
			final Users oldUser = usersTransactions.getUsersByUsername(user);

			if (!oldUser.getUsername().equals(registerBody.getUsername())) {
				usersTransactions.isUsername(registerBody.getUsername());
			}
			if (!oldUser.getContactMeta().getPhoneNo().equals(registerBody.getContactBody().getPhoneNo())) {
				usersTransactions.isPhoneNo(registerBody.getContactBody().getPhoneNo());
			}

			final Users users = usersTransactions.update(oldUser, registerBody.getUsername(),
					registerBody.getFirstName(), registerBody.getLastName(),
					Optional.fromNullable(registerBody.getRemark()),
					Optional.fromNullable(registerBody.getContactBody().getStreet()),
					Optional.fromNullable(registerBody.getContactBody().getCity()),
					Optional.fromNullable(registerBody.getContactBody().getState()),
					Optional.fromNullable(registerBody.getContactBody().getCountry()),
					Optional.fromNullable(registerBody.getContactBody().getPostalCode()),
					registerBody.getContactBody().getPhoneNo());

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<>(users.toShell(), HttpStatus.OK);

			LOG.info("updateProfile() response {}", responseEntity.toString());
			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("updateProfile() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("updateProfile() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<PreferenceContactShell>> preferenceContacts() {
		try {
			final SortedSet<PreferenceContactShell> preferenceContactShell = sudoersTransaction
					.getSortedPreferanceContacts();

			final ResponseEntity<SortedSet<PreferenceContactShell>> responseEntity = new ResponseEntity<>(
					preferenceContactShell, HttpStatus.OK);
			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("preferenceContacts() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("preferenceContacts() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> forgot(final String username, final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(username, "username can not be null.");
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");

		LOG.info("forgot() request with {}", username);

		try {

			final Users users = usersTransactions.getUsersByUsername(username);

			iMailService.asyncSendRegistration(String.format("%s %s", users.getFirstName(), users.getLastName()),
					users.getUsername(), users.getPassword());

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<>(users.toShell(), HttpStatus.OK);
			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("forgot() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("forgot() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<PreferenceTncShell> getTnc() {
		try {
			final PreferenceTnc preferenceTnc = sudoersTransaction.getTnc();

			final ResponseEntity<PreferenceTncShell> responseEntity = new ResponseEntity<>(preferenceTnc.toShell(),
					HttpStatus.OK);
			LOG.info("getTnc() response {}.", responseEntity.toString());
			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("getTnc() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("getTnc() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> subUser(final Users subuser) {
		Preconditions.checkNotNull(subuser, "subuser can not be null.");

		LOG.info("subUser() request with {}", subuser.toString());

		try {

			final Users users = usersTransactions.getUserBySubUser(subuser);
			final ResponseEntity<UsersShell> responseEntity;
			if (users == null) {
				responseEntity = new ResponseEntity<>(HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>(users.toShell(), HttpStatus.OK);
			}

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("subUser() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("subUser() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<UsersShell> addReferralCode(final Users users, final String referralCode) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(referralCode, "referralCode can not be null.");

		LOG.info("addReferralCode() request with {} with {}", users.toString(), referralCode);

		try {

			LOG.info(String.format("referralCode() request %s", referralCode));

			final Users refferdUser = usersTransactions.getUserByReferralCode(referralCode);

			if (!refferdUser.getActivateMeta().isActivate()) {
				throw new NotAccatable(MessageUtils.USER_IS_IN_ACTIVE);
			}

			final UsersShell userShell = usersTransactions.addSubUsers(refferdUser, users).toShell();

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<UsersShell>(userShell, HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("addReferralCode() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesUnauthorized e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesUnauthorized(error);
		} catch (Exception e) {
			final String error = String.format("addReferralCode() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

}
