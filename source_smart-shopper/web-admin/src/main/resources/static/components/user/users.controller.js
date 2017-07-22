/**
 * categories Table Controller.
 * 
 * @global
 */
var usersController = app.controller('UsersController', function($http, $scope,
		$rootScope, $state, $location, $element, DTDefaultOptions,
		DTOptionsBuilder, DTColumnDefBuilder, DefaultConstant, UtilityService,
		UserFactory, UserService) {

	$scope.labels = DefaultConstant.labels;
	var user = new User();
	user.clear();

	// for data table
	var vm = this;
	vm.users = user.users;
	UserFactory.set(user);
	var mainUser = UserFactory.get();

	$scope.collapseAll = function() {
		$scope.$broadcast('angular-ui-tree:collapse-all');
	};

	$scope.expandAll = function() {
		$scope.$broadcast('angular-ui-tree:expand-all');
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
			UtilityService.showError(response);
			return;
		}

		$scope.nodes = response;
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