package com.chillies.smartshopper.service.dto;

import java.util.SortedSet;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.shell.PreferenceTncShell;
import com.chillies.smartshopper.common.shell.web_admin.AuthShell;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.exception.ServicesUnauthorized;
import com.chillies.smartshopper.lib.model.web_model.PreferenceContact;
import com.chillies.smartshopper.lib.model.web_model.PreferenceTnc;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.service.manager.SessionManager;
import com.chillies.smartshopper.service.model.LoginBody;
import com.chillies.smartshopper.service.model.PreferenceContactBody;
import com.chillies.smartshopper.service.transaction.SudoersTransaction;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Sudoers : is for converts body-object to entity-object, body-object
 * validation and entity-object to shell-objects.
 * 
 * As well as all the business logic will be written in this class.
 * 
 * @author Jinen Kothari
 *
 */
@Service
public class SudoersDTO {

	private static final Logger LOG = LoggerFactory.getLogger(SudoersDTO.class);

	@Autowired
	private SudoersTransaction sudoersTransaction;

	@Autowired
	private SessionManager sessionManager;

	@PostConstruct
	private void init() {
		final Optional<PreferenceTnc> optional = Optional.fromNullable(sudoersTransaction.getTnc());
		if (!optional.isPresent()) {
			sudoersTransaction.save(new PreferenceTnc("Enter Terms and condition."));
		}
	}

	public ResponseEntity<AuthShell> auth(final LoginBody loginBody, final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(loginBody, "loginBody can not be null.");
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");

		try {
			LOG.info(String.format("auth() request %s", loginBody.toString()));

			final Sudoers sudoers = sudoersTransaction.getSudoers(loginBody.getUsername());

			if (!sudoers.getPassword().equals(loginBody.getPassword())) {
				throw new ServicesNotAcceptable(MessageUtils.USER_N_PASSWORD_INCORRECT);
			}

			httpRequest.getSession().invalidate();

			final String session = httpRequest.getSession(true).getId();
			final String user = sudoers.getUsername();

			final String userSessionCheck = sessionManager.getKey(user);

			if (userSessionCheck != null) {
				sessionManager.remove(userSessionCheck);
			}

			sessionManager.add(session, user);
			final ResponseEntity<AuthShell> responseEntity = new ResponseEntity<AuthShell>(
					new AuthShell(session, sudoers.toShell()), HttpStatus.OK);

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

	public ResponseEntity<AuthShell> isActive(final String session) {
		Preconditions.checkNotNull(session, "session can not be null.");

		try {

			LOG.info(String.format("isActive() request %s", session));

			final String user = sessionManager.getSessionUser(session);
			final Sudoers sudoers = sudoersTransaction.getSudoers(user);

			final ResponseEntity<AuthShell> responseEntity = new ResponseEntity<AuthShell>(
					new AuthShell(session, sudoers.toShell()), HttpStatus.OK);

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

	public Sudoers isValid(final String session) {
		Preconditions.checkNotNull(session, "username can not be null.");

		try {
			LOG.info(String.format("isValid() request %s", session));
			final String username = sessionManager.getSessionUser(session);

			final Sudoers sudoers = sudoersTransaction.getSudoers(username);
			LOG.info(String.format("isValid() response %s", sudoers.toString()));
			return sudoers;
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

	public ResponseEntity<PreferenceContactShell> addPreferanceContact(final Sudoers sudoers,
			final PreferenceContactBody preferenceContactBody) {
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(preferenceContactBody, "preferenceContactBody can not be null.");

		try {
			LOG.info("addPreferanceContact() request {} by user {}.", preferenceContactBody.toString(),
					sudoers.getUsername());

			final PreferenceContactShell preferenceContactShell;
			if (preferenceContactBody.getId() == null) {
				preferenceContactShell = sudoersTransaction
						.addPreferenceContact(sudoers, preferenceContactBody.getName(),
								preferenceContactBody.getEmailId(), preferenceContactBody.getPhoneNo())
						.toShell();
			} else {

				final Optional<PreferenceContact> optional = sudoersTransaction
						.getPreferenceContactById(preferenceContactBody.getId());

				if (!optional.isPresent()) {
					throw new NotAccatable(MessageUtils.PREFERENCE_IS_NOT_PRESENT);
				}

				preferenceContactShell = sudoersTransaction
						.editPreferenceContact(sudoers, optional.get(), preferenceContactBody.getName(),
								preferenceContactBody.getEmailId(), preferenceContactBody.getPhoneNo())
						.toShell();
			}

			final ResponseEntity<PreferenceContactShell> responseEntity = new ResponseEntity<>(preferenceContactShell,
					HttpStatus.OK);

			LOG.info("addPreferanceContact() response {}", responseEntity.toString());
			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("addPreferanceContact() Failed with message : %s", e.getMessage());
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
			final String error = String.format("addPreferanceContact() Failed with message : %s", e.getMessage());
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

	public ResponseEntity<PreferenceTncShell> updateTnc(final Sudoers sudoers, final String tnc) {
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		try {

			LOG.info("updateTnc() request {} by user {}.", tnc, sudoers.getUsername());

			final PreferenceTnc preferenceTnc = sudoersTransaction.getTnc();

			final PreferenceTncShell preferenceTncShell = sudoersTransaction.update(preferenceTnc, tnc).toShell();

			final ResponseEntity<PreferenceTncShell> responseEntity = new ResponseEntity<>(preferenceTncShell,
					HttpStatus.OK);
			LOG.info("updateTnc() response {}.", responseEntity.toString());
			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("updateTnc() Failed with message : %s", e.getMessage());
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
			final String error = String.format("updateTnc() Failed with message : %s", e.getMessage());
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

}
