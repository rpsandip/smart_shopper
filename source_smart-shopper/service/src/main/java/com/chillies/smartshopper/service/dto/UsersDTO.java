package com.chillies.smartshopper.service.dto;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.transaction.UsersTransactions;
import com.chillies.smartshopper.service.manager.SessionManager;
import com.chillies.smartshopper.service.model.LoginBody;
import com.chillies.smartshopper.service.model.RegisterBody;
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
	private SessionManager sessionManager;

	@Autowired
	private EmailValidator emailValidator;

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
				optionalParent = Optional.fromNullable(usersTransactions.getUserByReferralCode(registerBody.getParentId()));
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

			final ResponseEntity<SortedSet<UsersShell>> responseEntity = new ResponseEntity<SortedSet<UsersShell>>(
					sortedUsers, HttpStatus.OK);

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
			final Users oldUsers = usersTransactions.getUserById(userId);
			oldUsers.activateUser(sudoers);
			final Users users = usersTransactions.update(oldUsers);

			final ResponseEntity<UsersShell> responseEntity = new ResponseEntity<UsersShell>(users.toShell(),
					HttpStatus.OK);

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

}
