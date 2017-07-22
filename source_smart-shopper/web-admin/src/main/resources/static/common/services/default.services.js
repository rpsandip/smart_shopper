'use strict';

app.constant("DefaultConstant", {

	url : {
		SERVER_ADDRESS : "/services",
		SUDOERS : "/sudoers",
		USERS : "/users",
		ALL : "/all",
		ACTIVATE : "/activate",
		IS_ACTIVE : "/isActive",
		AUTH : "/auth",
		PREFERENCE : "/preference",
		PRODUCT : "/product",
		CATEGORY : "/category",
		SUPER : "/super",
		CATEGORIES : '/categories',
		ORDERS : '/orders',
		ORDER : '/order',
		STATUS : '/status',
		PRODUCTS : '/products',
		ADD : "/add",
		CONTACT : "/contact",
		TNC : "/tnc",
		CONTACTS : "/contacts",
		UPDATE : "/update",
		DELETE : '/delete',
		LOG_OUT : '/logout'
	},

	labels : {
		APP : 'Smart Shoppers Admin',
		LOGIN : 'Login',
		USERS : 'Users',
		EMAIL : 'Email',
		CONTACT : 'Contact',
		CONTACT_NO : 'Contact No',
		PREFERENCE : "Preference",
		USERNAME : 'Username',
		PASSWORD : 'Password',
		NAME : 'Name',
		DETAILS : 'Details',
		REFERRAL_CODE : 'Referral Code',
		ACTIVATE : 'Activate',
		PENDING : 'Pending',
		STATUS : 'Status',
		PREVIEW : 'Preview',
		ID : 'Id',
		ORDERS : 'Orders',
		TOTAL : 'Total',
		VIEW : 'View',
		CANCEL : 'Cancel',
		OUT_FOR_DELIVERY : 'Out for delivery',
		DELIVERED : 'Delivered',
		PLACED : 'Placed',
		DATE : 'Date',
		UPDATED : 'Updated',
		TERMS_N_CONDATION : 'Terms & conditions',
		VIEW_CHILD : 'View Child',
		CATEGORY : {
			SUPER_CATEGORY : 'Super Category',
			CATEGORY1 : 'Category',
			CATEGORY : 'Category',
			CATEGORIES : 'Categories'
		},
		COMMON : {
			DETAILS : 'Details',
			SAVE : 'Save',
			CODE : 'Code',
			NAME : 'Name',
			REMARK : 'Remarks',
			SEARCH : 'Search',
			EDIT : 'Edit',
			DELETE : 'Delete',
			ADD : 'Add',
			ACTIONS : 'Actions',
			YES : 'Yes',
			NO : 'No',
			CLOSE : 'Close',
			LOGOUT : 'Logout'
		},
		ITEM : {
			ITEM : 'Product',
			ITEMS : 'Products',
			IMAGE : 'Image',
			PRICE : 'Price',
			QUANTITY : 'Quantity',
			POINTS : 'Points'
		},
		ERROR : {
			is_required : 'is required.',
			REQUIRED : 'is Required.',
			INVALID_EMAIL : 'is Invalid.',
			DESC_MIN_LENGTH : 'at least 30 charters.',
			CODE_MIN_LENGTH : 'at least 5 charters.'
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

/**
 * Application Menus.
 * 
 * @global
 */
var menu = [ {
	ID : 0,
	TITLE : "Product",
	FACE : " icon-snowflake",
	CAN_VIEW : true
}, {
	ID : 1,
	TITLE : "Category",
	FACE : "icon-stack2",
	CAN_VIEW : false
}, {
	ID : 2,
	TITLE : "Users",
	FACE : " icon-users2",
	CAN_VIEW : false
}, {
	ID : 3,
	TITLE : "Orders",
	FACE : "icon-cart2",
	CAN_VIEW : false
} ];