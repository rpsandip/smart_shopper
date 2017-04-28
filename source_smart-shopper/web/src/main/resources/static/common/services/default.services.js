'use strict';

app.constant("DefaultConstant", {

	url : {
		SERVER_ADDRESS : "/services",
		SUDOERS : "/users",
		AUTH : "/auth",
		IS_ACTIVE : "/isActive",
		REGISTER : "/register",
		REFERRAL : '/referral',
		PRODUCT : '/product',
		PRODUCTS : '/products',
		CATEGORIES : '/categories',
		CATEGORY : '/category',
		LOG_OUT : '/logout'
	},

	labels : {
		APP : 'Smart Shopper',
		LOGIN : 'Login',
		USERNAME : 'Username or Email',
		PASSWORD : 'Password',
		PRODUCTS : 'Products',
		PRICE : 'Price',
		LOG_OUT : 'Logout',
		BUY : 'Buy',

		// registration
		REGISTRATION : 'Registration',
		REGISTR : 'Registr',
		REFERRAL : 'Referral',
		CANCEL : 'Cancel',
		FIRSTNAME : 'Firstname',
		SURNAME : 'Lastname',
		STREET : 'Street',
		CITY : 'City',
		STATE : 'State',
		COUNTRY : 'Country',
		PINCODE : 'Pincode',
		CONTACT_DETAILS : 'Contact details',
		PHONE_NO : 'Phone No',
		ERROR : {
			is_required : 'is required.'
		}
	}
});

app.factory('UtilityService', function($http, $rootScope, $mdToast) {
	var service = {};

	service.showError = function(message) {
		$rootScope.error = message;
		$mdToast.show({
			hideDelay : 10000,
			position : 'top right',
			controller : 'errorController',
			templateUrl : 'common/view/error.view.html'
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

var showSucess = function($mdDialog, message) {
	$mdDialog.show($mdDialog.alert().parent(
			angular.element(document.querySelector('#popupContainer')))
			.clickOutsideToClose(true).title(labels.SUCCESS).textContent(
					message).ariaLabel(labels.SUCCESS).ok(labels.OK)
			.targetEvent(null));
};

var showErrorDialouge = function($rootScope, $mdToast, message) {
	$rootScope.error = message;
	$mdToast.show({
		hideDelay : 10000,
		position : 'top right',
		controller : 'errorController',
		templateUrl : 'pages/component/error.html'
	});
};

var errorController = app.controller('errorController', function($http, $scope,
		$rootScope, $state, $location, $mdToast, DefaultConstant,
		UtilityService) {
	$scope.labels = DefaultConstant.labels;
	$scope.message = $rootScope.error;

	$scope.closeToast = function($event) {
		$mdToast.hide();
	};
});