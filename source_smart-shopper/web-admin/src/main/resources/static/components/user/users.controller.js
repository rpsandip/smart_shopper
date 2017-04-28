/**
 * categories Table Controller.
 * 
 * @global
 */
var usersController = app.controller('UsersController', function($http, $scope,
		$rootScope, $state, $location, $mdToast, $mdDialog, $element,
		DTDefaultOptions, DTOptionsBuilder, DTColumnDefBuilder,
		DefaultConstant, UtilityService, UserFactory, UserService) {

	var labels = DefaultConstant.labels;
	$scope.labels = labels;
	var user = new User();
	user.clear();

	// for data table
	var vm = this;
	vm.users = user.users;
	UserFactory.set(user);
	var mainUser = UserFactory.get();

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(300)
			.withDOM('trp').withBootstrap().withScroller();
	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('#').withOption(
					'autoWidth', true),
			DTColumnDefBuilder.newColumnDef(1).withTitle(labels.USERNAME)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(2).withTitle(labels.NAME)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(3).withTitle(labels.REFERRAL_CODE)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(4).withTitle(labels.STATUS)
					.withOption('autoWidth', true) ];

	$scope.activate = function($event, user) {
		if (user == undefined || user == null) {
			UtilityService.showError("User is not selected.");
			return;
		}
		$scope.isLoading = true;
		UserService.activate(user.ID, function(response, status) {
			$scope.isLoading = false;
			$scope.user.ACTIVATE_META.activate = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.user.ACTIVATE_META.activate = true;
		});

	};

	// list all the categories
	$scope.isLoading = true;
	UserService.users(function(response, status) {
		$scope.isLoading = false;
		if (status == 401) {
			UtilityService.showError(response.message);
			return;
		}
		if (status != 200) {
			UtilityService.showError(response.message);
			return;
		}
		for (i in response) {
			user.fromJSON(response[i]);
		}
	});

});
//
app.factory('UserFactory', function() {
	var factory = {};
	var user;

	factory.set = function(value) {
		user = value;
	};

	factory.get = function() {
		return user;
	};

	return factory;
});