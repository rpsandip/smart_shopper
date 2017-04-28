package com.chillies.smartshopper.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.web.UsersAuthShell;
import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.chillies.smartshopper.service.dto.UsersDTO;
import com.chillies.smartshopper.service.model.LoginBody;
import com.chillies.smartshopper.service.model.RegisterBody;

/**
 * UsersService : is @RestController for user login, logout & session
 * management.
 * 
 * @author Jinen Kothari
 *
 */
@RestController
@RequestMapping(value = "${service}${users}")
public class UsersService {

	@Autowired
	private UsersDTO usersDTO;

	@RequestMapping(value = "${service.users.register}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersShell> register(@Valid @RequestBody RegisterBody registerBody,
			HttpServletRequest httpRequest) {
		return usersDTO.register(registerBody, httpRequest);
	}

	@RequestMapping(value = "${service.users.referral}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersShell> referralCode(@RequestParam(value = "referralCode", required = true) String referralCode) {
		return usersDTO.referralCode(referralCode);
	}

	// auth services
	@RequestMapping(value = "${service.users.auth}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersAuthShell> auth(@Valid @RequestBody LoginBody loginBody,
			HttpServletRequest httpRequest) {
		return usersDTO.auth(loginBody, httpRequest);
	}

	@RequestMapping(value = "${service.users.isActive}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersAuthShell> isActive(@RequestParam(value = "session", required = true) String session) {
		return usersDTO.isActive(session);
	}

	@RequestMapping(value = "${service.users.logout}", method = RequestMethod.POST)
	public ResponseEntity<Object> logout(@RequestParam(value = "session", required = true) String session) {
		usersDTO.logout(session);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
