'use strict';

app.constant("DefaultConstant", {

	url : {
		SERVER_ADDRESS : "/services",
		SUDOERS : "/users",
		GET_PROFILE : '/getProfile',
		AUTH : "/auth",
		IS_ACTIVE : "/isActive",
		REGISTER : "/register",
		updateProfile : "/updateProfile",
		REFERRAL : '/referral',
		PRODUCT : '/product',
		PRODUCTS : '/products',
		CATEGORIES : '/categories',
		CATEGORY : '/category',
		CART : '/cart',
		ORDER : '/order',
		ORDERS : '/orders',
		CANCEL : '/cancel',
		PLACE : '/place',
		SEARCH : '/search',
		ADD : '/add',
		REMOVE : '/remove',
		GET : '/get',
		SUB_USER : '/subUser',
		LOG_OUT : '/logout',
		FORGOT : '/forgot',
		PREFERENCE : "/preference",
		CONTACTS : "/contacts",
		TNC : "/tnc"
	},

	labels : {
		APP : 'Smart Shoppers',
		LOGIN : 'Login',
		USERNAME : 'Username or Email',
		PASSWORD : 'Password',
		EMAIL : 'Email',
		CONTACT : 'Contact',
		PRODUCTS : 'Products',
		PRICE : 'Price',
		LOG_OUT : 'Logout',
		NEXT : 'Next',
		BACK : 'Back',
		APPLY : 'Apply',
		REFERRAL_CODE : 'Referral Code',
		ADD_TO_CART : 'Add to cart',
		BUY : 'Buy',
		CART : 'Cart',
		USER : 'User',
		GROCERY : 'Grocery',
		HOME : 'Home',
		ORDERS : 'Orders',
		DETAILS : 'Details',
		PREVIEW : 'Preview',
		CHECK_OUT : 'Check out',
		REMOVE : 'Remove',
		QUANTITY : 'Quantity',
		ORDER : 'Order',
		TOTAL : 'Total',
		STATUS : 'Status',
		ID : 'Id',
		PLACED : 'Placed',
		DATE : 'Date',
		UPDATED : 'Updated',
		UPDATE : 'Update',
		VIEW : 'View',
		PROFILE : 'Profile',
		COSMETIC : 'Cosmetic',
		NATURAL_SUPPLEMENT : 'Natural Supplement',
		OUR_BUSINESS_PARTNER : 'Our Business Partners',
		EMPTY_TABLE : 'No data available',
		SEARCH : 'Search:  ',
		SEARCH1 : 'Search',
		T_FIRST : '&#xab;',
		T_LAST : '&#xbb;',
		T_NEXT : '&#x2192;',
		T_PREVIOUS : '&#x2190;',
		SETTINGS : 'Settings',
		FRIENDS : 'Friends',
		ACTIVATE : 'ACTIVATE',
		TERMS_N_CONDATION : 'Terms & conditions',

		// registration
		FORGOT_PASSWORD : 'forgot password',
		REGISTRATION : 'Registration',
		REGISTR : 'Registr',
		REFERRAL : 'Referral',
		CANCEL : 'Cancel',
		FIRSTNAME : 'Firstname',
		SURNAME : 'Lastname',
		NAME : 'Name',
		STREET : 'Street',
		CITY : 'City',
		STATE : 'State',
		COUNTRY : 'Country',
		PINCODE : 'Pincode',
		CONTACT_DETAILS : 'Contact Details',
		PHONE_NO : 'Phone No',
		ERROR : {
			is_required : 'is required.',
			EMAIL : 'Invalid email'
		}
	}
});

app.factory('UtilityService', function($http, $rootScope) {
	var service = {};

	service.showError = function(message) {
		$rootScope.error = message;
		new PNotify({
			title : 'Error occurred',
			addclass : 'bg-danger',
			text : message
		});
	}
	return service;

});

/**
 * getUserTimeZone i.e. browser timezoen.
 * 
 * @global
 */
var getUserTimeZone = function() {
	var timeZone = jstz.determine().name();
	return "America/Los_Angeles";
}

/**
 * Application date and time format.
 * 
 * @global
 */
var dateFormat = 'MMM DD YYYY  HH:mm:ss';
var longFormat = 'YYYYMMDD';

var getDateToLong = function(date) {
	return moment(date).format(longFormat);
};