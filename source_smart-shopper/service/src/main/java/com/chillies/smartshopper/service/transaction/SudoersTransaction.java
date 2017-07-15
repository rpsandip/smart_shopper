package com.chillies.smartshopper.service.transaction;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.shell.PreferenceContactShell;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web_model.PreferenceContact;
import com.chillies.smartshopper.lib.model.web_model.PreferenceTnc;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.service.IWebAdminDbService;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * SudoersTransaction : is for database transaction for @Sudoers
 * 
 * @author Jinen Kothari
 *
 */
@Service
public final class SudoersTransaction {

	private static final Logger LOG = LoggerFactory.getLogger(SudoersTransaction.class);

	@Autowired
	private IWebAdminDbService dbService;

	@PostConstruct
	private void init() throws DbException {

		final Optional<Sudoers> optionalAdmin = Optional.fromNullable(dbService.byUsername("admin"));
		if (!optionalAdmin.isPresent()) {
			dbService.save(new Sudoers("admin", "admin", "Smart", "Shopper Admin", new DateMeta(Optional.absent()),
					Optional.absent()));
		}
		final Optional<Sudoers> optionalSys = Optional.fromNullable(dbService.byUsername("sys"));

		if (!optionalSys.isPresent()) {
			dbService.save(
					new Sudoers("sys", "sys", "Smart", "Shopper", new DateMeta(Optional.absent()), Optional.absent()));
		}
	}

	public Sudoers getSudoers(final String username) throws DbException, NotAccatable {
		Preconditions.checkNotNull(username, "username can not be null.");

		final Optional<Sudoers> optional = Optional.fromNullable(dbService.byUsername(username));

		if (!optional.isPresent()) {
			final String error = MessageUtils.userNotRegistered(username);
			LOG.info(String.format("getSudoers() %s is not register.", username));
			throw new NotAccatable(error);
		}
		return optional.get();
	}

	private PreferenceContact save(final PreferenceContact preferenceContact) {
		Preconditions.checkNotNull(preferenceContact, "preferenceContact can not be null.");

		return dbService.save(preferenceContact);
	}

	public PreferenceContact addPreferenceContact(final Sudoers sudoers, final String name, final String emailId,
			final String phoneNo) {
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(emailId, "emailId can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");
		return this.save(new PreferenceContact(name, phoneNo, emailId, sudoers));
	}

	public SortedSet<PreferenceContactShell> getSortedPreferanceContacts() {
		final SortedSet<PreferenceContactShell> contactShells = new TreeSet<>(
				Comparator.comparing(PreferenceContactShell::getId));
		dbService.preferenceContacts().forEach(contact -> contactShells.add(contact.toShell()));
		return contactShells;
	}

	public Optional<PreferenceContact> getPreferenceContactById(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");

		return dbService.findPreferenceById(id);
	}

	public PreferenceContact editPreferenceContact(final Sudoers sudoers, final PreferenceContact preferenceContact,
			final String name, final String emailId, final String phoneNo) {
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(preferenceContact, "preferenceContact can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(emailId, "emailId can not be null.");
		Preconditions.checkNotNull(phoneNo, "phoneNo can not be null.");

		preferenceContact.update(name, emailId, phoneNo, sudoers);
		return dbService.save(preferenceContact);
	}

	public PreferenceTnc getTnc() {
		return dbService.latestPreferenceTnc();
	}

	public PreferenceTnc save(final PreferenceTnc preferenceTnc) {
		Preconditions.checkNotNull(preferenceTnc, "preferenceTnc can not be null.");
		return dbService.save(preferenceTnc);
	}

	public PreferenceTnc update(final PreferenceTnc preferenceTnc,final String tnc) {
		Preconditions.checkNotNull(preferenceTnc, "preferenceTnc can not be null.");
		Preconditions.checkNotNull(tnc, "tnc can not be null.");
		preferenceTnc.update(tnc);
		return dbService.save(preferenceTnc);
	}
}
