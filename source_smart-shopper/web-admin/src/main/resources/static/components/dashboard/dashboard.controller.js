/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('DashboardController', function($http,
		$scope, $rootScope, $state, $location, DefaultConstant, UtilityService,
		AuthenticationService) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	$scope.labels = DefaultConstant.labels;
	$scope.menus = menu;
	$scope.USER = AuthenticationService.getName();

	$scope.onLogout = function($event) {
		AuthenticationService.doLogout();
	};

	$scope.isLoading = false;

	$scope.onSelectionMenu = function($event, menu) {
		if (!menu) {
			UtilityService.showError("No menu has been selected.");
			return;
		}

		$scope.CURRENT_MENU = menu.TITLE;
		switch (menu.ID) {
		case 0:
			$state.go('dashboard.product');
			break;
		case 1:
			$state.go('dashboard.category');
			break;
		case 2:
			$state.go('dashboard.user');
			break;
		case 3:
			$state.go('dashboard.order');
			break;
		default:
			$state.go('dashboard.product');
			break;
		}

	};
});

var orderController = app.controller('OrderController',
		function($http, $scope, $rootScope, $state, $location, $window,
				DefaultConstant, DTDefaultOptions, DTOptionsBuilder,
				DTColumnDefBuilder, ProductService) {

			var labels = DefaultConstant.labels;
			$scope.isProductDetails = false;

			var vm = this;
			vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
					.withDOM('ftp');

			$scope.onProductDetailsClose = function($event) {
				$scope.isProductDetails = false;
			};
			$scope.onOrderStatusChange = function($event, status, order) {
				if (!order) {
					UtilityService.showError("No order is selected.");
					return;
				}

				$scope.isLoading = true;
				ProductService.orderStatus(order.id, status, function(response,
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
					$window.location.reload();
				});
			};

			$scope.onOrderView = function($event, order) {
				if (!order) {
					UtilityService.showError("No order is selected.");
					return;
				}
				$scope.isProductDetails = true;
				console.log(order.users);
				$scope.products = order.cart.products;
				$scope.user = order.users;
			};

			$scope.isLoading = true;
			ProductService.orders(function(response, status) {

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