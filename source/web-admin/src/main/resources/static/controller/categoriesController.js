/**
 * categories Table Controller.
 * 
 * @global
 */
var categoriesController = app
		.controller(
				'categoriesController',
				function($http, $scope, $rootScope, $state, $location,
						$mdToast, $mdDialog, $element, CategoryService,
						CategoryFactory, ItemService, DTDefaultOptions,
						DTOptionsBuilder, DTColumnDefBuilder) {

					$scope.labels = labels;
					var category = new Category();
					category.clear();
					$scope.isCategory = false;

					// Selection
					$scope.clearSearchTerm = function() {
						$scope.searchTerm = '';
					};

					$scope.updateSearch = function(ev) {
						ev.stopPropagation();
					};

					// Table Value
					var vm = this;
					vm.categories = category.categories;
					CategoryFactory.setCategory(category);

					vm.dtOptions = DTOptionsBuilder.newOptions()
							.withDisplayLength(300).withDOM('tr')
							.withBootstrap().withScroller();
					vm.dtColumnDefs = [
							DTColumnDefBuilder.newColumnDef(0).withTitle(
									labels.CATEGORY.CATEGORY).withOption(
									'autoWidth', true),
							DTColumnDefBuilder.newColumnDef(1).withTitle(
									labels.ITEM.ITEMS).withOption('autoWidth',
									true),
							DTColumnDefBuilder.newColumnDef(2).withTitle('')
									.withOption('autoWidth', true) ];

					vm.onAddClick = function($event) {

						$scope.isCategory ? $scope.isCategory = false
								: $scope.isCategory = true;
						var mainCategory = CategoryFactory.getCategory();
						mainCategory.isEdit = false;

						$scope.category = new Category();
					};

					vm.onEdit = function($event, $index, viewCategory) {
						if (!viewCategory) {
							showErrorDialouge($rootScope, $mdToast,
									"category is not selected!");
							return;
						}

						$scope.isCategory ? $scope.isCategory = false
								: $scope.isCategory = true;

						var mainCategory = CategoryFactory.getCategory();
						mainCategory.editRow(viewCategory);
						$scope.category = mainCategory;
						vm.selectedItem = mainCategory.items;

					};

					vm.onDelete = function($event, $index, viewCategory) {
						if (!viewCategory) {
							showErrorDialouge($rootScope, $mdToast,
									"Category is not selected!");
							return;
						}

						var mainCategory = CategoryFactory.getCategory();
						var textContent = "Are you sure you want to delete this "
								+ viewCategory.NAME + ".";

						// Appending dialog to document.body to cover sidenav in
						// docs app
						var confirm = $mdDialog.confirm().title('Delete!')
								.textContent(textContent).ariaLabel(
										mainCategory.NAME).targetEvent($event)
								.ok(labels.COMMON.YES).cancel(labels.COMMON.NO);

						$mdDialog
								.show(confirm)
								.then(
										function() {

											viewCategory.DELETED = true;
											mainCategory.editRow(viewCategory);

											$scope.isLoading = true;
											CategoryService
													.addOrUpdate(
															mainCategory
																	.toJSON(),
															function(response,
																	status) {
																$scope.isLoading = false;

																if (status != 200) {
																	if (status == -1) {
																		response = "Server is down! Contact admin."
																	}
																	showErrorDialouge(
																			$rootScope,
																			$mdToast,
																			response);
																	return;
																}
																mainCategory
																		.fromJSON(response);
																$mdDialog
																		.cancel();
															});
										}, function() {
											$mdDialog.cancel();
										});

					};

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
					});

					$scope.isLoading = true;
					ItemService.getItems(function(response, status) {
						$scope.isLoading = false;

						if (status != 200) {
							if (status == -1) {
								response = "Server is down! Contact admin."
							}
							showErrorDialouge($rootScope, $mdToast, response);
							return;
						}

						var item = new Item();
						item.clear();

						for (i = 0; i < response.length; i++) {
							item.fromJSON(response[i]);
						}

						$scope.ITEMS = item.items;
					});

					$scope.onSave = function($event, category, selectedItem) {

						if (!category) {
							showErrorDialouge($rootScope, $mdToast,
									"category can not be empty!");
							return;
						}

						if (!selectedItem) {
							showErrorDialouge($rootScope, $mdToast,
									"select few Items for this category.");
							return;
						}

						var mainCategory = CategoryFactory.getCategory();

						category.items = selectedItem;

						$scope.isLoading = true;
						CategoryService
								.addOrUpdate(
										category.toJSON(),
										function(response, status) {
											$scope.isLoading = false;

											if (status != 200) {
												if (status == -1) {
													response = "Server is down! Contact admin."
												}
												showErrorDialouge($rootScope,
														$mdToast, response);
												return;
											}
											mainCategory.fromJSON(response);
											$scope.category = new Category();
										});

					};

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