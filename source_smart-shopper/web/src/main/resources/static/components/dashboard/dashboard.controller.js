/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('DashboardController', function($http,
		$scope, $rootScope, $state, $location, $window, DTDefaultOptions,
		DTOptionsBuilder, DTColumnDefBuilder, AuthenticationService,
		DefaultConstant, UtilityService, ProductServices, CategoryFactory) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	CategoryFactory.setCategory(category);
	$scope.categories = category.categories;

	var cart = $scope.cart = new Cart();

	AuthenticationService.isLoggedIn(function(response) {
		if (!response) {
			$scope.isLoggedIn = false;
		} else {
			$scope.isLoggedIn = true;
			$scope.USER = AuthenticationService.getName();

			$scope.isLoading = true;
			ProductServices
					.getCart(function(response, status) {
						$scope.isLoading = false;
						if (status == 401) {
							UtilityService.showError(response.message);
							return;
						}
						if (status != 200) {
							UtilityService.showError(response.message);
							return;
						}

						if (response == null || response == undefined
								|| response == "") {
							$scope.isCart = false;
						} else {
							$scope.isCart = true;
						}

						cart.fromJSON(response);
					});

		}
	});

	$scope.onLogout = function($event) {
		AuthenticationService.doLogout();
		$scope.isLoggedIn = false;
		$window.location.reload();
	};

	$scope.onTopCategoryMenu = function($event) {
		$state.go('dashboard.categories');
	};

	$scope.onTopOrderMenu = function($event) {
		$state.go('dashboard.orders');
	};

	$scope.onTopHomeMenu = function($event) {
		$state.go('dashboard.home');
	};

	$scope.onProductPopClick = function($event, product) {
		if (product == undefined || product == null) {
			UtilityService.showError("User is not logged-in.");
			return;
		}
		$scope.productPop = product;
	};

	var product;
	if ($rootScope.product != undefined || $rootScope.product != null) {
		product = $rootScope.product;
	} else {
		product = new Product();
	}

	// for data table
	var vm = this;
	vm.products = product.products;

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp')
	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('').withOption(
					'autoWidth', true).notSortable(),
			DTColumnDefBuilder.newColumnDef(1).withTitle(labels.PRODUCTS)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(2).withTitle(labels.PRICE)
					.withOption('autoWidth', true) ];

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
			$scope.CATEGORY_TITLE = category.NAME;
		});
	};

	$scope.onAddToCart = function($event, product) {

		if (!$scope.isLoggedIn) {
			UtilityService.showError("User is not logged-in.");
			return;
		}

		if (product == undefined || product == null) {
			UtilityService.showError("No product is selected.");
			return;
		}

		$scope.isCart = true;
		$scope.isLoading = true;
		ProductServices.addToCart(cart.toJSON(product, 1), function(response,
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
			cart.fromJSON(response);
		});
	};

	$scope.removeFromCart = function($event, product) {
		if (!$scope.isLoggedIn) {
			UtilityService.showError("User is not logged-in.");
			return;
		}

		if (product == undefined || product == null) {
			UtilityService.showError("No product is selected.");
			return;
		}

		ProductServices.remove(product.product.id, function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			cart.fromJSON(response);
		});
	};

	$scope.onCheckOut = function($event) {
		if (!$scope.isLoggedIn) {
			UtilityService.showError("User is not logged-in.");
			return;
		}
		ProductServices.place(function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			cart.fromJSON(response);
			cart = new Cart();
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
		$http, $scope, $rootScope, $state, $location, DefaultConstant,
		ProductServices, CategoryFactory) {

	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	CategoryFactory.setCategory(category);

	$scope.myInterval = 3000;
	$scope.slides = [ {
		image : 'resource/img/ss_title_1.jpg'
	}, {
		image : 'resource/img/ss_title_2.jpg'
	}, {
		image : 'resource/img/ss_title_3.jpg'
	}, {
		image : 'resource/img/ss_title_4.jpg'
	} ];

	$scope.categories = category.categories;
	$scope.isLoggedIn = false;

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
		console.log(category.categories)
	});

	$scope.onProductClick = function($event) {
		$state.go('dashboard.categories');
	};
});

var orderController = app.controller('OrderController', function($http, $scope,
		$rootScope, $state, $location, $window, DefaultConstant,
		ProductServices, DTDefaultOptions, DTOptionsBuilder,
		DTColumnDefBuilder, CategoryFactory) {
	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	var labels = $scope.labels = DefaultConstant.labels;

	var vm = this;
	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp');

	$scope.cancelOrder = function($event, order) {

		if (order == undefined || order == null) {
			UtilityService.showError("No order is selected.");
			return;
		}

		$scope.isLoading = true;
		ProductServices.cancel(order.id, function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$window.location.reload();
		});
	};

	$scope.isLoading = true;
	ProductServices.orders(function(response, status) {

		$scope.isLoading = false;
		if (status == 401) {
			UtilityService.showError(response.message);
			return;
		}
		if (status != 200) {
			UtilityService.showError(response.message);
			return;
		}

		vm.orders = response;
	});

});

app.factory('CategoryFactory', function() {
	var factory = {};
	var category;

	factory.setCategory = function(value) {
		category = value;
	};

	factory.getCategory = function() {
		return category;
	};

	return factory;
});