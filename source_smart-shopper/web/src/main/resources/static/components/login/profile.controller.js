app
		.controller(
				'ProfileController',
				function($http, $scope, $rootScope, $state, $location, $window,
						DefaultConstant, UtilityService, DTDefaultOptions,
						DTOptionsBuilder, DTColumnDefBuilder, UsersService) {
					var labels = $scope.label = DefaultConstant.labels;
					var register = $scope.registration = new Registration();

					$scope.collapseAll = function() {
						$scope.$broadcast('angular-ui-tree:collapse-all');
					};

					$scope.expandAll = function() {
						$scope.$broadcast('angular-ui-tree:expand-all');
					};

					$scope.onUpdate = function(registration) {
						if (registration == undefined || registration == null) {
							UtilityService
									.showError("Registration can not be null.");
							return;
						}

						$scope.isLoading = true;
						UsersService
								.updateProfile(
										registration.toJSON(null),
										function(response, status) {

											$scope.isLoading = false;
											if (status == 401) {
												UtilityService
														.showError(response.message);
												return;
											}
											if (status != 200) {
												UtilityService
														.showError(response.message);
												return;
											}
											new PNotify(
													{
														title : 'Congratulations',
														addclass : 'bg-success',
														text : 'Your Account has been updated successfully. Please Logout and login for better experience.'
													});
										});
					};

					$scope.isLoading = true;
					UsersService.getProfile(function(response, status) {
						$scope.isLoading = false;
						if (status == 401) {
							UtilityService.showError(response.message);
							return;
						}
						if (status != 200) {
							UtilityService.showError(response.message);
							return;
						}
						register.fromJSON(response)
						$scope.nodes = register.subUsers;
					});

					$scope.isLoading = true;
					UsersService.subUser(function(response, status) {
						$scope.isLoading = false;
						if (status == 401) {
							UtilityService.showError(response.message);
							return;
						}
						if (status != 200) {
							UtilityService.showError(response.message);
							return;
						}
						$scope.refferUser = response;
					});

					$scope.onReferral = function($event, referralCode) {
						if (referralCode == undefined || referralCode == null) {
							UtilityService
									.showError("Add any referral code.");
							return;
						}
						$scope.isLoading = true;
						UsersService.referralAdd(referralCode, function(response,
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
							$scope.refferUser = response;
						});
					};

				});