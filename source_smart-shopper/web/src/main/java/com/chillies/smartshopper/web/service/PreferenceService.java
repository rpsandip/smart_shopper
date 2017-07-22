package com.chillies.smartshopper.web.service;

import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.shell.PreferenceTncShell;
import com.chillies.smartshopper.service.dto.UsersDTO;

@RestController
@RequestMapping(value = "${service}${preference}")
public class PreferenceService {

	@Autowired
	private UsersDTO usersDTO;

	// Product services
	@GetMapping(value = "${service.preference.contacts}")
	public ResponseEntity<SortedSet<PreferenceContactShell>> preferenceContacts() {
		return usersDTO.preferenceContacts();
	}
	
	@GetMapping(value = "${service.preference.tnc}")
	public ResponseEntity<PreferenceTncShell> getTnc() {
		return usersDTO.getTnc();
	}
}
