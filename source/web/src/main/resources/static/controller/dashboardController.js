/**
 * Dasboard Controller.
 * 
 * @global
 */
var dashboardController = app.controller('dashboardController', function($http,
		$scope, $rootScope, $state, $location, $mdSidenav, $mdToast,
		DefaultConstant) {

	$scope.toolbarTitle = document.title = DefaultConstant.labels.APP;
	$scope.labels = labels;
	$scope.menus = menu;

	$scope.onMenuClick = function($event) {
		$mdSidenav('left').toggle();
	};

	$scope.onSelectionMenu = function($event, menu) {
		if (!menu) {
			showErrorDialouge($rootScope, $mdToast,
					"No menu has been selected!");
			return;
		}
		switch (menu.ID) {
		case 0:
			$state.go('dashboard.item');
			break;
		case 1:
			$state.go('dashboard.category');
			break;
		case 2:
			$state.go('dashboard.order');
			break;
		default:
			$state.go('dashboard.menu');
			break;
		}

	};
});