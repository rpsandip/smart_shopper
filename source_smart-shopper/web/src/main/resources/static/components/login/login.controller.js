'use strict';
app.controller('LoginController', function($http, $scope, $window, $rootScope,
		$state, $location, $mdToast, $mdDialog, AuthenticationService,
		UtilityService, UsersService, DefaultConstant) {

	$scope.label = DefaultConstant.labels;
	$scope.isLoading = false;
	if ($rootScope.isRegister == null || $rootScope.isRegister == undefined) {
		$scope.isRegister = false;
	} else {
		if ($rootScope.isRegister) {
			$scope.isRegister = true;
			$scope.registration = new Registration();
		} else {
			$scope.isRegister = false;
		}
	}

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

			$state.go('dashboard.categories');
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
		});
	};

	$scope.isReferralCode = false;
	$scope.onReferral = function($event) {
		var confirm = $mdDialog.prompt().title('Referral Code').textContent('')
				.placeholder('Enter referral code').ariaLabel('').initialValue(
						'').targetEvent($event).ok('Ok').cancel('cancel');

		$mdDialog.show(confirm).then(function(result) {
			$scope.isLoading = true;
			UsersService.referral(result, function(response, status) {
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
				$scope.referralCode = response;
			});
		}, function() {
			$scope.isReferralCode = false;
		});
	};

});