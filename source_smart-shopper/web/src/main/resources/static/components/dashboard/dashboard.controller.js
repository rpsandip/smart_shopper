/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('DashboardController', function($http,
		$scope, $rootScope, $state, $location, $window, DTDefaultOptions,
		DTOptionsBuilder, DTColumnDefBuilder, AuthenticationService,
		DefaultConstant, UtilityService, ProductServices, CategoryFactory,
		ProductFactory) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	CategoryFactory.setCategory(category);
	$scope.categories = category.categories;

	// for data table
	var vm = this;
	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp');

	var mainProduct = new Product();
	ProductFactory.set(mainProduct);
	mainProduct.clear();
	vm.products = mainProduct.products;

	var cart = $scope.cart = new Cart();

	AuthenticationService.isLoggedIn(function(response, data) {
		if (!response) {
			$scope.isLoggedIn = false;
		} else {
			$scope.isLoggedIn = true;
			$scope.user = data.user;
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
			mainProduct.clear();
			for (i in response) {
				mainProduct.fromJSON(response[i]);
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
		ProductServices.addToCart(cart.toJSON(product.ID, 1), function(
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
			var message = product.product.name + " is removed from cart.";
			new PNotify({
				title : 'Info',
				addclass : 'bg-info',
				text : message
			});
		});
	};

	$scope.$on('categoryUpdated', function(event, category) {
		$scope.CATEGORY_TITLE = category.NAME;
	});

	$scope.$on('addToCartFromCard', function(event, product) {
		if ($scope.$$listenerCount["addToCartFromCard"] > 1) {
			$scope.$$listenerCount["addToCartFromCard"] = 0;
		}
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
		ProductServices.addToCart(cart.toJSON(product.id, 1), function(
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
			cart.fromJSON(response);
			var message = product.name + " is add to cart.";
			new PNotify({
				title : 'Info',
				addclass : 'bg-info',
				text : message
			});
			$window.location.reload();
		});
	});

	$scope.onCheckOut = function($event) {
		if (!$scope.isLoggedIn) {
			UtilityService.showError("User is not logged-in.");
			return;
		}

		$scope.isLoading = false;
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
			var message = "Your order of $" + response.total
					+ " is placed Successfully."
					+ "<p><small>Pay at the time of delivery.</small></p>";
			new PNotify({
				title : 'Order confirmed',
				addclass : 'bg-success',
				delay : 10000,
				text : message
			});

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
		$http, $scope, $rootScope, $window, $state, $location, DefaultConstant,
		ProductServices, CategoryFactory, ProductFactory) {

	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();

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
	});

	$scope.onAddToCart = function($event, product) {
		if (product == undefined || product == null) {
			UtilityService.showError("No product is selected.");
			return;
		}

		$rootScope.$broadcast('addToCartFromCard', product);
	};

	$scope.onProductClick = function($event) {
		$state.go('dashboard.categories');
	};

	$scope.onCategorySelection = function($event, category) {

		if (category == undefined || category == null) {
			UtilityService.showError("No category is selected.");
			return;
		}

		var product = ProductFactory.get();
		product.clear();

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
			$rootScope.$broadcast('categoryUpdated', category);
			$state.go('dashboard.categories');
		});
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

	$scope.onOrderView = function($event, order) {
		if (!order) {
			UtilityService.showError("No order is selected.");
			return;
		}
		$scope.isProductDetails = true;
		$scope.products = order.cart.products;
	};

	$scope.onProductDetailsClose = function($event) {
		$scope.isProductDetails = false;
	};

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

app.factory('ProductFactory', function() {
	var factory = {};
	var product;

	factory.set = function(value) {
		product = value;
	};

	factory.get = function() {
		return product;
	};

	return factory;
});