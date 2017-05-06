'use strict';
app.controller('LoginController', function($http, $scope, $window, $rootScope,
		$state, $location, AuthenticationService, UtilityService, UsersService,
		DefaultConstant, WizardHandler) {

	$scope.label = DefaultConstant.labels;
	$scope.isLoading = false;
	$scope.isRegister = false;

	(function initController() {
		// reset login status
		AuthenticationService.clearCredentials();
	})();

	$scope.onAdminPortalClicked = function() {
		$window.open('http://localhost:13000');
	};

	$scope.login = function(username, password) {
		$scope.isLoading = true;
		AuthenticationService.doLogin(username, password, function(response,
				status) {

			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}

			AuthenticationService.setCredentials(response.session,
					response.user.username, response.user.firstName + " "
							+ response.user.lastName);

			$('#modal_default').modal('toggle');
			$window.location.reload();
		});
	};

	$scope.onRegistration = function() {
		$scope.isRegister = true;
		$scope.registration = new Registration();
	};

	$scope.cancelRegistration = function() {
		$scope.isRegister = false;
	};

	$scope.onRegister = function(registration, referralCode) {
		if (registration == undefined || registration == null) {
			UtilityService.showError("Registration can not be null.");
			return;
		}
		$scope.isLoading = true;
		UsersService.register(registration.toJSON(referralCode), function(
				response, status) {

			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.isRegister = false;
			$scope.registration = new Registration();
			WizardHandler.wizard().reset();
			WizardHandler.wizard().finish();

		});
	};

	$scope.isReferralCode = false;

	$scope.onReferral = function($event, referralCode) {
		$scope.isLoading = true;
		UsersService.referral(referralCode, function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.isReferralCode = true;
			$scope.referralCode = referralCode;
			$scope.referralUser = response;
		});
	};

});