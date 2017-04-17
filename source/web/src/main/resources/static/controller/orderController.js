/**
 * Item Table Controller.
 * 
 * @global
 */
var orderController = app.controller('orderController',
		function($http, $scope, $rootScope, $state, $location, $mdToast,
				$mdDialog, CategoryService, CategoryFactory, DTDefaultOptions,
				DTOptionsBuilder, DTColumnDefBuilder) {

			$scope.labels = labels;
			var category = new Category();
			category.clear();
			
			$scope.categories = category.categories;
			
			$scope.isLoading = true;
			CategoryService.getCategories(function(response, status) {
				$scope.isLoading = false;

				if (status != 200) {
					if (status == -1) {
						response = "Server is down! Contact admin."
					}
					showErrorDialouge($rootScope, $mdToast, response);
					return;
				}

				for (i in response) {
					category.fromJSON(response[i]);
				}
				//
			});

		});