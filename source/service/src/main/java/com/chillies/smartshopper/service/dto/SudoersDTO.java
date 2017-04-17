package com.chillies.smartshopper.service.dto;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.AuthShell;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.model.Sudoers;
import com.chillies.smartshopper.lib.transaction.SudoersTransaction;
import com.chillies.smartshopper.service.manager.SessionManager;
import com.chillies.smartshopper.service.model.LoginBody;
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

}
