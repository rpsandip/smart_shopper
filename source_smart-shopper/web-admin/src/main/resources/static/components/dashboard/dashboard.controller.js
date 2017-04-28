/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('DashboardController', function($http,
		$scope, $rootScope, $state, $location, $mdSidenav, $mdToast, $mdPanel,
		$mdDialog, DefaultConstant, AuthenticationService) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	$scope.labels = DefaultConstant.labels;
	$scope.menus = menu;
	$scope.USER = AuthenticationService.getName();

	$scope.onLogout = function($event) {
		AuthenticationService.doLogout();
	};

	$scope.onMenuClick = function($event) {
		$mdSidenav('left').toggle();
	};

	$scope.onSelectionMenu = function($event, menu) {
		if (!menu) {
			showErrorDialouge($rootScope, $mdToast,
					"No menu has been selected!");
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
			$state.go('dashboard.menu');
			break;
		}

	};

	$scope.openMenu = function($mdOpenMenu, $event) {
		$mdOpenMenu($event);
	};

});