package com.chillies.smartshopper.web_admin.service;

import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.web.UsersShell;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.service.dto.SudoersDTO;
import com.chillies.smartshopper.service.dto.UsersDTO;

/**
 * UsersService : is @RestController for user login, logout & session
 * management.
 * 
 * @author Jinen Kothari
 *
 */
@RestController
@RequestMapping(value = "${service}${sudoers}${users}")
public class UsersService {

	@Autowired
	private UsersDTO usersDTO;

	@Autowired
	private SudoersDTO sudoersDTO;

	@RequestMapping(value = "${service.sudoers.users.all}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<UsersShell>> all(@RequestParam(value = "session", required = true) String session) {
		sudoersDTO.isValid(session);
		return usersDTO.all();
	}

	@RequestMapping(value = "${service.sudoers.users.activate}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersShell> activate(@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "userId", required = true) String userId) {
		final Sudoers sudoers = sudoersDTO.isValid(session);
		return usersDTO.activate(userId, sudoers);
	}

}
