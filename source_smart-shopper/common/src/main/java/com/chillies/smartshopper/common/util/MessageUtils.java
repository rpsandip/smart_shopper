package com.chillies.smartshopper.common.util;

import com.google.common.base.Preconditions;

public final class MessageUtils {

	public static final String USER_N_PASSWORD_INCORRECT = "Username or password is incorrect.";

	public static final String USERNAME_ALREADY_EXISTS = "Username alredy exists.";

	public static final String PHONE_NO_ASSOCIATED_WITH_OTHER_USERNAME = "Phone no is associated with other username.";

	public static final String EMAIL_NOT_VALID = "Email address is not valid.";

	public static final String USER_SESSION_EXPIRED = "User session has been expired.";

	public static final String PRODUCT_CATEGORY_IS_NOT_PRESENT = "Product category is not present.";

	public static final String REQUIRED_FIELD = "Required field can not be empty.";

	public static final String PRODUCT_CATEGORY_ALREADY_EXISTS = "Product category already exists.";

	public static final String PRODUCT_NAME_ALREADY_EXISTS = "Product already exists.";

	public static final String IMAGE_TYPE = "Image must PNG type.";

	public static final String IMAGE_SIZE = "Image size must be < 400 kb";

	public static final String PRODUCT_IS_NOT_PRESENT = "Product is not present.";

	public static final String USER_NOT_REGISTER = "User is not register.";

	public static final String USER_IS_IN_ACTIVE = "User is not active.";

	public static final String CART_AVAIABLE = "No Cart is present.";

	public static final String ORDER_NOT_PRESENT = "Order is not present.";

	// admin user
	public static String userNotRegistered(final String username) {
		Preconditions.checkNotNull(username, "Please enter username.");

		return String.format("Username %s is not registered.", username);
	}

	public static String referralCodeNotPresent(final String referralCode) {
		Preconditions.checkNotNull(referralCode, "Referral code is not present.");

		return String.format("Referral-Code %s is not associated with any user.", referralCode);
	}

	public static String fileNotExists(final String productCode) {
		Preconditions.checkNotNull(productCode, "Product code is not present.");

		return String.format("Product image %s is not exists.", productCode);
	}

}
