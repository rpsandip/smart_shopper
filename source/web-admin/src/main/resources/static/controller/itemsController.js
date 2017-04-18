/**
 * Item Controller.
 * 
 * @global
 */
var itemsController = app.controller('itemsController', function($http, $scope,
		$rootScope, $state, $location, $mdToast, $mdDialog, ItemService,
		ItemFactory) {

	$scope.labels = labels;
	var item = new Item();
	item.clear();

	$scope.isItem = false;

	$scope.openMenu = function($mdOpenMenu, ev) {
		originatorEv = ev;
		$mdOpenMenu(ev);
	};

	// Table Value
	var vm = this;
	vm.items = item.items;

	// setting value to Item- factory
	ItemFactory.setItem(item);

	vm.onAddClick = function($event) {
		$scope.isItem ? $scope.isItem = false : $scope.isItem = true;
		$scope.item = new Item();
	};

	vm.onDelete = function($event, $index, viewItem) {
		if (!viewItem) {
			showErrorDialouge($rootScope, $mdToast, "Item is not selected!");
			return;
		}

		var mainItem = ItemFactory.getItem();

		var textContent = "Are you sure you want to delete this "
				+ viewItem.NAME + ".";

		// Appending dialog to document.body to cover sidenav in
		// docs app
		var confirm = $mdDialog.confirm().title('Delete!').textContent(
				textContent).ariaLabel(viewItem.NAME).targetEvent($event).ok(
				labels.COMMON.YES).cancel(labels.COMMON.NO);

		$mdDialog.show(confirm).then(
				function() {
					
					viewItem.DELETED = true;
					mainItem.editRow(viewItem);

					$scope.isLoading = true;
					ItemService.addOrUpdate(mainItem.toJSON(), image, function(
							response, status) {
						$scope.isLoading = false;

						if (status != 200) {
							if (status == -1) {
								response = "Server is down! Contact admin."
							}
							showErrorDialouge($rootScope, $mdToast, response);
							return;
						}
						mainItem.fromJSON(response);
					});
				}, function() {
					$mdDialog.cancel();
				});
	};

	vm.onEdit = function($event, $index, viewItem) {
		if (!item) {
			showErrorDialouge($rootScope, $mdToast, "Item is not selected!");
			return;
		}

		var mainItem = ItemFactory.getItem();
		mainItem.editRow(viewItem);
		$scope.isItem ? $scope.isItem = false : $scope.isItem = true;
		$scope.item = item;
	};

	var image = null;

	$scope.onSave = function($event, item) {
		if (!item || !item.code || !item.name ) {
			showErrorDialouge($rootScope, $mdToast, "Item can not be empty!");
			return;
		}
		
		var mainItem = ItemFactory.getItem();

		$scope.isLoading = true;
		ItemService.addOrUpdate(item.toJSON(), image,
				function(response, status) {

					$scope.isLoading = false;

					if (status != 200) {
						if (status == -1) {
							response = "Server is down! Contact admin."
						}
						showErrorDialouge($rootScope, $mdToast, response);
						return;
					}

					mainItem.fromJSON(response);
					$scope.item = new Item();
				});
	};

	$scope.uploadFile = function(file) {
		if (!file) {
			showErrorDialouge($rootScope, $mdToast,
					"No image has been selected.");
			return;
		}

		if (file.size >= 25000) {
			showErrorDialouge($rootScope, $mdToast,
					"Image size must be < 25 kb.");
			return;
		}

		image = file;
	};

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

		for (i = 0; i < response.length; i++) {
			item.fromJSON(response[i]);
		}
	});
});

app.factory('ItemFactory', function() {
	var factory = {};
	var item;

	factory.setItem = function(value) {
		item = value;
	};

	factory.getItem = function() {
		return item;
	};

	return factory;
});
