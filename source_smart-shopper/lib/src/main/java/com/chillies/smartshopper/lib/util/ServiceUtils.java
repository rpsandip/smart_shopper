package com.chillies.smartshopper.lib.util;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Preconditions;

public final class ServiceUtils {

	private final static String PRODUCT_URL = "/services/product/productURL";

	public static String siteBaseUrl(final HttpServletRequest httpRequest) {
		Preconditions.checkNotNull(httpRequest, "httpRequest can not be null.");

		return String.format("%s://%s:%s%s%s", httpRequest.getScheme(), httpRequest.getServerName(),
				httpRequest.getServerPort(), httpRequest.getContextPath(), PRODUCT_URL);
	}
}
