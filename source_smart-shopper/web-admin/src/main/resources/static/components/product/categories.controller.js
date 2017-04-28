/**
 * categories Table Controller.
 * 
 * @global
 */
var categoriesController = app.controller('CategoriesController', function(
		$http, $scope, $rootScope, $state, $location, $mdToast, $mdDialog,
		$element, DTDefaultOptions, DTOptionsBuilder, DTColumnDefBuilder,
		DefaultConstant, UtilityService, CategoryFactory, ProductService) {

	var labels = DefaultConstant.labels;
	$scope.labels = labels;
	var category = new Category();
	category.clear();
	$scope.isCategory = false;

	// for data table
	var vm = this;
	vm.categories = category.categories;
	CategoryFactory.setCategory(category);
	var mainCategory = CategoryFactory.getCategory();

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(300)
			.withDOM('trp').withBootstrap().withScroller();
	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('#').withOption('autoWidth', false).notSortable(),
			DTColumnDefBuilder.newColumnDef(1).withTitle(
					labels.CATEGORY.CATEGORY).withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(2).withTitle('').withOption(
					'autoWidth', false) ];

	// on add or close click
	vm.onAddClick = function($event) {
		$scope.isCategory ? $scope.isCategory = false
				: $scope.isCategory = true;
		var mainCategory = CategoryFactory.getCategory();
		mainCategory.isEdit = false;

		$scope.category = new Category();
	};

	// on save click
	$scope.onSave = function($event, category) {
		if (category == undefined) {
			UtilityService.showError("Category can not be null.");
			return;
		}
		$scope.isLoading = true;
		ProductService.addCategory(category.toJSON(),
				function(response, status) {
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