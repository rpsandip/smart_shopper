package com.chillies.smartshopper.web_admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.web_admin.AuthShell;
import com.chillies.smartshopper.service.dto.SudoersDTO;
import com.chillies.smartshopper.service.model.LoginBody;

/**
 * SudoersService : is @RestController for user login, logout & session
 * management.
 * 
 * @author Jinen Kothari
 *
 */
@RestController
@RequestMapping(value = "${service}${sudoers}")
public class SudoersService {

	@Autowired
	private SudoersDTO sudoersDTO;

	@RequestMapping(value = "${service.sudoers.auth}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthShell> auth(@Valid @RequestBody LoginBody loginBody, HttpServletRequest httpRequest) {		
		return sudoersDTO.auth(loginBody, httpRequest);
	}

}
