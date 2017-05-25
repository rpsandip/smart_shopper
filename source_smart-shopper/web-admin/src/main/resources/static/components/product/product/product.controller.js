/**
 * product Controller.
 * 
 * @global
 */
var productController = app.controller('ProductController', function($http,
		$scope, $rootScope, $state, $location, $element, $window,
		DTDefaultOptions, DTOptionsBuilder, DTColumnDefBuilder, ProductFactory,
		DefaultConstant, UtilityService, ProductService) {

	var labels = $scope.labels = DefaultConstant.labels;
	var product = new Product();
	product.clear();
	$scope.isProduct = false;

	var vm = this;
	vm.products = product.products;
	ProductFactory.setProduct(product);

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp');

	vm.dtColumnDefs = [
			DTColumnDefBuilder.newColumnDef(0).withTitle('').withOption(
					'autoWidth', false),
			DTColumnDefBuilder.newColumnDef(1).withTitle('').withOption(
					'autoWidth', false).notSortable(),
			DTColumnDefBuilder.newColumnDef(2).withTitle(labels.ITEM.ITEM)
					.withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(3).withTitle(
					labels.CATEGORY.CATEGORY1).withOption('autoWidth', true),
			DTColumnDefBuilder.newColumnDef(4).withTitle('').withOption(
					'autoWidth', false).notSortable() ];

	var mainProduct = ProductFactory.getProduct();

	$scope.onAddClick = function($event) {
		$scope.isProduct = true;
		$scope.product = new Product();

		var category = new Category();
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
			$scope.categories = category.categories;
		});
	};

	// on add or close click
	$scope.onEditClick = function($event, product) {
		if (product == undefined) {
			UtilityService.showError("Product can not be null.");
			return;
		}
		$scope.isProduct = true;

		var category = new Category();
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
			$scope.categories = category.categories;

			var mainProduct = ProductFactory.getProduct();
			mainProduct.isEdit = true;
			mainProduct.editProduct(product);
			$scope.product = mainProduct;
		});

	};

	$scope.onCloseClick = function($event) {
		$scope.isProduct = false;
	};

	$scope.onSave = function($event, product, image) {
		if (!product) {
			UtilityService.showError("Product can not be empty.");
			return;
		}

		if (!product.isEdit) {
			if (!image) {
				UtilityService.showError("No image has been selected.");
				return;
			}

			$scope.isLoading = true;
			ProductService.addProduct(product.toJSON(), image, function(
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
				$scope.isProduct = false
				$scope.product = new Product();
				mainProduct.fromJSON(response);
			});
		} else {
			$scope.isLoading = true;
			ProductService.updateProduct(product.toJSON(), image, function(
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
				$window.location.reload();
			});
		}
	};

	$scope.uploadFile = function(file) {
		if (!file) {
			UtilityService.showError("No image has been selected.");
			return;
		}

		if (file.size >= 400000) {
			UtilityService.showError("Image size must be < 400 kb.");
			return;
		}

		$scope.image = file;
	};

	// list all the categories
	$scope.$parent.$parent.isLoading = true;
	ProductService.products(function(response, status) {
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
			product.fromJSON(response[i]);
		}
	});

});

app.factory('ProductFactory', function() {
	var factory = {};
	var product;

	factory.setProduct = function(value) {
		product = value;
	};

	factory.getProduct = function() {
		return product;
	};

	return factory;
});
