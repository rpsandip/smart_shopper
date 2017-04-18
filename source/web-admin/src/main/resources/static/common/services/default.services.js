'use strict';

app.constant("DefaultConstant", {

	url : {
		SERVER_ADDRESS : "/services",
		SUDOERS : "/sudoers",
		AUTH : "/auth",
	},

	labels : {
		APP : 'Smart-Shopper Admin',
		LOGIN : 'Login',
		USERNAME : 'Username',
		PASSWORD : 'Password',
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

/**
 * Application Labels
 * 
 * @global
 */
var labels = {
	COMMON : {
		DETAILS : 'Details',
		SAVE : 'Save',
		CODE : 'Code',
		NAME : 'Name',
		DESCRIPTION : 'Description',
		SEARCH : 'Search',
		EDIT : 'Edit',
		DELETE : 'Delete',
		ADD : 'Add',
		ACTIONS : 'Actions',
		YES : 'Yes',
		NO : 'No',
		CLOSE : 'Close'
	},
	ITEM : {
		PREFIX : 'ITM',
		ITEM : 'Item',
		ITEMS : 'Items',
		IMAGE : 'Image',
		PRICE : 'Price'
	},
	CATEGORY : {
		PREFIX : 'CAT',
		CATEGORY : 'Category',
		CATEGORIES : 'Categories'
	},
	ERROR : {
		REQUIRED : 'is Required!',
		DESC_MIN_LENGTH : 'at least 30 charters.',
		CODE_MIN_LENGTH : 'at least 5 charters.'
	}

};

/**
 * Application Menus.
 * 
 * @global
 */
var menu = [ {
	ID : 0,
	TITLE : "Item",
	FACE : "fa fa-tag",
	CAN_VIEW : true
}, {
	ID : 1,
	TITLE : "Category",
	FACE : "fa fa-tags",
	CAN_VIEW : false
}, {
	ID : 2,
	TITLE : "Orders",
	FACE : "fa fa-first-order",
	CAN_VIEW : false
} ];

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
		$rootScope, $state, $location, $mdToast) {
	$scope.labels = labels;
	$scope.message = $rootScope.error;

	$scope.closeToast = function($event) {
		$mdToast.hide();
	};
});

var checkAuthorized = function($state, status) {
	if (status == 401) {
		$state.go('login');
	}
};