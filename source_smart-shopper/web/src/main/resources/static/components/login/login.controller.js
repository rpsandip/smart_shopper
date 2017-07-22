'use strict';
app
		.controller(
				'LoginController',
				function($http, $scope, $window, $rootScope, $state, $location,
						AuthenticationService, UtilityService, UsersService,
						DefaultConstant, WizardHandler) {

					$scope.label = DefaultConstant.labels;
					$scope.isLoading = false;
					$scope.isRegister = false;

					$scope.onAdminPortalClicked = function() {
						$window.open('http://localhost:13000');
					};

					$scope.login = function(username, password) {
						$scope.isLoading = true;
						AuthenticationService.doLogin(username, password,
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

									AuthenticationService.setCredentials(
											response.session,
											response.user.username,
											response.user.firstName + " "
													+ response.user.lastName);

									$('#modal_default').modal('toggle');
								});
					};

					$scope.onForgotPasswordClick = function($event, username) {
						if (username == undefined || username == null) {
							UtilityService
									.showError("Please fill your register username.");
							return;
						}

						UsersService
								.forgot(
										username,
										function(response, status) {

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
														title : 'Info',
														addclass : 'bg-info',
														text : "Email has been sent to your register Email address."
													});

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
							UtilityService
									.showError("Registration can not be null.");
							return;
						}

						$scope.isLoading = true;
						UsersService
								.register(
										registration.toJSON(referralCode),
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
											$scope.isRegister = false;
											$scope.isReferralCode = false;
											$scope.registration = new Registration();
											WizardHandler.wizard().reset();
											WizardHandler.wizard().finish();
											new PNotify(
													{
														title : 'Congratulations',
														addclass : 'bg-success',
														text : 'Your Account has been registered successfully. Please Login to your account.'
													});

											$("a[href='#basic-tab1']").click();

										});
					};

					$scope.isReferralCode = false;

					$scope.onReferral = function($event, referralCode) {
						$scope.isLoading = true;
						UsersService.referral(referralCode, function(response,
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
							$scope.isReferralCode = true;
							$scope.referralCode = referralCode;
							$scope.referralUser = response;
						});
					};

				});