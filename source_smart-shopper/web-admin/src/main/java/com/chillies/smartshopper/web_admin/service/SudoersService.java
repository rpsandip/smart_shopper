package com.chillies.smartshopper.web_admin.service;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.shell.PreferenceTncShell;
import com.chillies.smartshopper.common.shell.web_admin.AuthShell;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.service.dto.SudoersDTO;
import com.chillies.smartshopper.service.model.LoginBody;
import com.chillies.smartshopper.service.model.PreferenceContactBody;
import com.chillies.smartshopper.service.model.PreferenceTncBody;

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

	// auth services
	@RequestMapping(value = "${service.sudoers.auth}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthShell> auth(@Valid @RequestBody LoginBody loginBody, HttpServletRequest httpRequest) {
		return sudoersDTO.auth(loginBody, httpRequest);
	}

	@RequestMapping(value = "${service.sudoers.isActive}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthShell> isActive(@RequestParam(value = "session", required = true) String session) {
		return sudoersDTO.isActive(session);
	}

	@RequestMapping(value = "${service.sudoers.logout}", method = RequestMethod.POST)
	public ResponseEntity<Object> logout(@RequestParam(value = "session", required = true) String session) {
		sudoersDTO.logout(session);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Preference
	@RequestMapping(value = "${service.sudoers.preference.add.contact}", method = RequestMethod.POST)
	public ResponseEntity<PreferenceContactShell> addPreferanceContact(
			@RequestParam(value = "session", required = true) String session,
			@Valid @RequestBody PreferenceContactBody preferenceContactBody) {
		final Sudoers sudoers = sudoersDTO.isValid(session);

		return sudoersDTO.addPreferanceContact(sudoers, preferenceContactBody);
	}

	@RequestMapping(value = "${service.sudoers.preference.contacts}", method = RequestMethod.GET)
	public ResponseEntity<SortedSet<PreferenceContactShell>> preferenceContacts(
			@RequestParam(value = "session", required = true) String session) {
		sudoersDTO.isValid(session);

		return sudoersDTO.preferenceContacts();
	}

	@PostMapping(value = "${service.sudoers.preference.update.tnc}")
	public ResponseEntity<PreferenceTncShell> updateTnc(
			@RequestParam(value = "session", required = true) String session,
			@Valid @RequestBody PreferenceTncBody tncBody) {
		final Sudoers sudoers = sudoersDTO.isValid(session);

		return sudoersDTO.updateTnc(sudoers, tncBody.getTnc());
	}

	@GetMapping(value = "${service.sudoers.preference.tnc}")
	public ResponseEntity<PreferenceTncShell> getTnc(@RequestParam(value = "session", required = true) String session) {
		sudoersDTO.isValid(session);
		return sudoersDTO.getTnc();
	}
}
