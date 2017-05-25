package com.chillies.smartshopper.lib.transaction;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.model.DateMeta;
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
		final Optional<Sudoers> optionalTest = Optional.fromNullable(dbService.byUsername("test"));
		if (!optionalTest.isPresent()) {
			dbService.save(new Sudoers("test", "test", "Smart", "Shopper Tester", new DateMeta(Optional.absent()),
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
}
