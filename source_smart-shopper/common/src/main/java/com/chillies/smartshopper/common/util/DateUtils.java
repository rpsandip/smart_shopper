package com.chillies.smartshopper.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Preconditions;

/**
 * DateUtils : is utility class for Date-time.
 * 
 * @author Jinen Kothari
 *
 */
public final class DateUtils {

	private final static DateTimeFormatter DATE_TIME_ZONE = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss Z");

	private final static DateTimeFormatter PATH = DateTimeFormat.forPattern("yyyyMMdd-HHmmssSSS");

	private final static DateTimeFormatter DISPLAY = DateTimeFormat.forPattern("E, d MMMM yyyy");

	public static DateTime currentUTC() {
		return DateTime.now(DateTimeZone.UTC);
	}

	public static String toString(final DateTime dateTime, final boolean timeZone) {
		Preconditions.checkNotNull(dateTime, "dateTime can not be null.");
		if (timeZone) {
			return DATE_TIME_ZONE.print(dateTime);
		}
		
		return DISPLAY.print(dateTime);
	}

	public static String dateTimeToAddon() {
		return PATH.print(currentUTC());
	}

}
