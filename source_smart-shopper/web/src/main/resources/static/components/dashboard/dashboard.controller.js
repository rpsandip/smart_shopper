/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('DashboardController', function($http,
		$scope, $rootScope, $state, $location, $mdSidenav, $mdToast,
		DTDefaultOptions, DTOptionsBuilder, DTColumnDefBuilder,
		AuthenticationService, DefaultConstant, ProductServices) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	$scope.menus = category.categories;

	$scope.onMenuClick = function($event) {
		$mdSidenav('flet').toggle();
	};

	AuthenticationService.isLoggedIn(function(response) {
		if (!response) {
			$scope.isLoggedIn = false;
		} else {
			$scope.isLoggedIn = true;
			$scope.USER = AuthenticationService.getName();
		}
	});

	$scope.onLogout = function($event) {
		AuthenticationService.doLogout();
		$scope.isLoggedIn = false;
	};

	$scope.openMenu = function($mdOpenMenu, $event) {
		$mdOpenMenu($event);
	};

	$scope.onLogin = function($event) {
		$rootScope.isRegister = false;
		$state.go('login');
	};

	$scope.onRegister = function($event) {
		$rootScope.isRegister = true;
		$state.go('login');
	};

	var product = new Product();
	// for data table
	var vm = this;
	$scope.products = product.products;

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(50).withDOM(
			'ftrp').withBootstrap().withScroller();
	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('').withOption(
					'autoWidth', true).notSortable(),
			DTColumnDefBuilder.newColumnDef(1).withTitle(labels.PRODUCTS)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(2).withTitle(labels.PRICE)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(3).withTitle('').withOption(
					'autoWidth', true) ];

	$scope.onSelectionMenu = function($event, category) {
		if (category == undefined || category == null) {
			UtilityService.showError("No category is selected.");
			return;
		}

		$scope.isLoading = true;
		ProductServices.categorizedProduct(category.ID, function(response,
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
			product.clear();
			for (i in response) {
				product.fromJSON(response[i]);
			}
		});
	};

	$scope.isLoading = true;
	ProductServices.categories(function(response, status) {

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
			category.fromJSON(response[i]);
		}
	});

});

var dashboardController = app.controller('DashboardCardController', function(
		$http, $scope, $rootScope, $state, $location, $mdSidenav, $mdToast,
		DefaultConstant, ProductServices) {
	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	$scope.categories = category.categories;

	$scope.isLoggedIn = false;

	$scope.onLogin = function($event) {
		$rootScope.isRegister = false;
		$state.go('login');
	};

	$scope.onRegister = function($event) {
		$rootScope.isRegister = true;
		$state.go('login');
	};

	$scope.isLoading = true;
	ProductServices.categorizedProducts(function(response, status) {

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
			category.fromJSON(response[i]);
		}
	});

	$scope.onCategorySelection = function($event, category) {

		if (category == undefined || category == null) {
			UtilityService.showError("No category is selected.");
			return;
		}

		$scope.isLoading = true;
		ProductServices.categorizedProduct(category.ID, function(response,
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
			$state.go('dashboard.categories');
//			product.clear();
//			for (i in response) {
//				product.fromJSON(response[i]);
//			}
		});
		
	};

});