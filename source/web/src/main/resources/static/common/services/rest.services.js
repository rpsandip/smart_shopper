/**
 * getHeadersForJsonType to get JSON header.
 * 
 * @global
 */
var getHeadersForJsonType = function() {
	return "Conten-Type : application/json;charset=UTF-8"
};

/**
 * REST services Prefix.
 * 
 * @global
 */
var PREFIX = "/services";

var getItemsURL = function() {
	return PREFIX + "/" + "getItems";
};

var getCategoriesURL = function() {
	return PREFIX + "/" + "getCategories";
};

var getAddOrUpdateItemURL = function(item) {
	return PREFIX + "/" + "addOrUpdateItem" + "?itemDTO=" + JSON.stringify(item);
};

var getAddOrUpdateCategoryURL = function() {
	return PREFIX + "/" + "addOrUpdateCategory"
};

/**
 * Item
 * 
 * @global
 */
app.factory('ItemService', function($http) {
	var service = {};

	service.getItems = function(callback) {
		var url = getItemsURL();
		$http.get(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.addOrUpdate = function(itemDTO, image, callback) {
		var url = getAddOrUpdateItemURL(itemDTO);
		var formdata = new FormData();
		formdata.append('image', image)

		$http.post(url, formdata, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}
	return service;
});

/**
 * Category
 * 
 * @global
 */
app.factory('CategoryService', function($http) {
	var service = {};

	service.getCategories = function(callback) {
		var url = getCategoriesURL();
		$http.get(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.addOrUpdate = function(categortDTO, callback) {
		var url = getAddOrUpdateCategoryURL();
		$http.post(url, categortDTO).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}
	return service;
});