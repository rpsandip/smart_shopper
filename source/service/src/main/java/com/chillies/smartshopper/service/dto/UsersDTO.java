package com.chillies.smartshopper.service.dto;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.UsersShell;
import com.chillies.smartshopper.common.util.EmailValidator;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.model.Users;
import com.chillies.smartshopper.lib.transaction.UsersTransactions;
import com.chillies.smartshopper.service.manager.SessionManager;
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

	public ResponseEntity<UsersShell> register(final RegisterBody registerBody, final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(registerBody, "registerBody can not be null.");
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");
		try {
			LOG.info(String.format("register() request %s", registerBody.toString()));

			final String username = registerBody.getUsername();

			if (emailValidator.validate(username)) {
				throw new NotAccatable(MessageUtils.EMAIL_NOT_VALID);
			}
			final String phoneNo = registerBody.getContactBody().getPhoneNo();

			usersTransactions.isUsername(username);
			usersTransactions.isPhoneNo(phoneNo);

			final Users users = usersTransactions.save(username, registerBody.getPassword(),
					registerBody.getFirstName(), registerBody.getLastName(),
					Optional.fromNullable(registerBody.getRemark()),
					Optional.fromNullable(registerBody.getContactBody().getStreet()),
					Optional.fromNullable(registerBody.getContactBody().getCity()),
					Optional.fromNullable(registerBody.getContactBody().getState()),
					Optional.fromNullable(registerBody.getContactBody().getCountry()),
					Optional.fromNullable(registerBody.getContactBody().getPostalCode()), phoneNo);

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
}
