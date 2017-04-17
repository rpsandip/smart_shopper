package com.chillies.smartshopper.common.util;

import java.io.File;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * AppUtil : is utility class to set mindmaze_home in application.
 * 
 * @author Jinen Kothari
 *
 */
public final class AppUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class);

	private static String HOME;

	public static final String SMART_SHOPPER = "Smart.Shopper.Home";

	public static final String PATH_SEPARATOR = File.separator;

	public static final String CHAR_TYPE = "UTF-8";

	public static void setHOME(final String home) {
		Preconditions.checkNotNull(home, "home can not be null.");

		LOG.info("setHOME() Smart-shopper home setted @ {}", home);

		HOME = home;
	}

	public static String getHOME() {
		return HOME;
	}

	public static String getNewReferralCode() {
		final Random random = new Random();
		final Integer referral = 1000000 + random.nextInt(900000);
		return referral.toString();
	}

}
