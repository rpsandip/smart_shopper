'use strict';
app.controller('LoginController', function($http, $scope, $rootScope, $state,
		$location, AuthenticationService, UtilityService, DefaultConstant) {

	$scope.label = DefaultConstant.labels;
	$scope.isLoading = false;

	(function initController() {
		// reset login status
		AuthenticationService.clearCredentials();
	})();

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
			console.log(AuthenticationService.getSession())
			console.log(AuthenticationService.getUsername())
			console.log(AuthenticationService.getName())

			$state.go('dashboard.product');
		});
	};

});