/**
 * getHeadersForJsonType to get JSON header.
 * 
 * @global
 */
var getHeadersForJsonType = function() {
	return "Conten-Type : application/json;charset=UTF-8"
};

app.factory('UsersService', function($http, DefaultConstant) {
	var service = {};

	service.register = function(payload, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.REGISTER;

		$http.post(url, payload).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.referral = function(referralCode, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.REFERRAL
				+ "?referralCode=" + referralCode;

		$http.post(url).success(function(Response, Status, Headers, Config) {
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
app.factory('ProductServices', function($http, DefaultConstant) {
	var service = {};

	service.categories = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORIES;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.categorizedProduct = function(categoryId, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.PRODUCT + "?categoryId=" + categoryId;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}
	
	service.categorizedProducts = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.PRODUCTS;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}
	return service;
});