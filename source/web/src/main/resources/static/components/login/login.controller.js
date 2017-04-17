'use strict';
app.controller('LoginController', function($http, $scope, $rootScope, $state,
		$location, $mdToast, $mdDialog, AuthenticationService, UtilityService,
		DefaultConstant) {

	$scope.label = DefaultConstant.labels;
	$scope.isLoading = false;
	$scope.isRegister = false;

	(function initController() {
		// reset login status
		AuthenticationService.clearCredentials();
	})();

	$scope.login = function(username, password) {
		$scope.isLoading = true;
		AuthenticationService.doLogin(username, password, function(response,
				status) {

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
			console.log(AuthenticationService.getSession())
			console.log(AuthenticationService.getUsername())
			console.log(AuthenticationService.getName())
			$scope.isLoading = false;
			$state.go('dashboard.item');
		});
	};

	$scope.registration = function() {
		$scope.isRegister = true;
	};

	$scope.cancelRegistration = function() {
		$scope.isRegister = false;
	};

});