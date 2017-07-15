/**
 * categories Table Controller.
 * 
 * @global
 */
var categoriesController = app.controller('CategoriesController', function(
		$http, $scope, $rootScope, $state, $window, $location, $element,
		DTDefaultOptions, DTOptionsBuilder, DTColumnDefBuilder,
		DefaultConstant, UtilityService, CategoryFactory, ProductService) {

	var labels = $scope.labels = DefaultConstant.labels;
	var category = new Category();
	category.clear();
	$scope.isCategory = false;

	// for data table
	var vm = this;
	vm.categories = category.categories;
	CategoryFactory.setCategory(category);
	var mainCategory = CategoryFactory.getCategory();

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp').withOption('order', [ 1, 'asc' ]);
	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('#').withOption(
					'autoWidth', false),
			DTColumnDefBuilder.newColumnDef(1).withTitle(
					labels.CATEGORY.CATEGORY).withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(2).withTitle('').withOption(
					'autoWidth', true) ];

	// on add or close click
	$scope.onAddClick = function($event) {
		$scope.isCategory = true;
		var mainCategory = CategoryFactory.getCategory();
		mainCategory.isEdit = false;

		$scope.category = new Category();
		ProductService.superCategories(function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.superCategories = response;
		});
	};

	// on add or close click
	$scope.onEditClick = function($event, category) {
		if (category == undefined) {
			UtilityService.showError("Category can not be null.");
			return;
		}
		$scope.isCategory = true;

		ProductService.superCategories(function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.superCategories = response;

			var mainCategory = CategoryFactory.getCategory();
			mainCategory.isEdit = true;
			mainCategory.editCategory(category);
			$scope.category = mainCategory
		});
	};

	$scope.onDeleteClick = function($event, category) {
		if (category == undefined) {
			UtilityService.showError("Category can not be null.");
			return;
		}

		$scope.isLoading = true;
		ProductService.deleteCategory(category.ID, function(response, status) {
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

	$scope.onCloseClick = function($event) {
		$scope.isCategory = false;
	};

	// on save click
	$scope.onSave = function($event, category) {
		if (category == undefined) {
			UtilityService.showError("Category can not be null.");
			return;
		}
		if (!category.isEdit) {
			$scope.isLoading = true;
			ProductService.addCategory(category.toJSON(), function(response,
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
				$scope.isCategory = false;
				$scope.category = new Category();
				mainCategory.fromJSON(response);
			});
		} else {
			$scope.isLoading = true;
			ProductService.updateCategory(category.toJSON(), function(response,
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
		}
	};

	// list all the categories
	$scope.isLoading = true;
	ProductService.categories(function(response, status) {
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
//
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