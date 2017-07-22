/**
 * product & Category
 * 
 * @global
 */
app.factory('ProductService', function($http, DefaultConstant,
		AuthenticationService) {
	var service = {};

	service.addCategory = function(payload, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.ADD + "?session="
				+ AuthenticationService.getSession();
		$http.post(url, payload).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.updateCategory = function(payload, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.UPDATE + "?session="
				+ AuthenticationService.getSession();
		$http.post(url, payload).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.deleteCategory = function(categoryId, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.DELETE + "?session="
				+ AuthenticationService.getSession() + "&categoryId="
				+ categoryId;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.superCategories = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.SUPER
				+ DefaultConstant.url.CATEGORIES + "?session="
				+ AuthenticationService.getSession();
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.categories = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.CATEGORY
				+ DefaultConstant.url.CATEGORIES + "?session="
				+ AuthenticationService.getSession();
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.addProduct = function(payload, productImage, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.ADD
				+ "?session=" + AuthenticationService.getSession()
				+ "&productBody=" + JSON.stringify(payload);
		var formdata = new FormData();
		formdata.append('productImage', productImage);
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

	service.updateProduct = function(payload, productImage, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.UPDATE
				+ "?session=" + AuthenticationService.getSession()
				+ "&productBody=" + JSON.stringify(payload);
		var formdata = new FormData();
		formdata.append('productImage', productImage);
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

	service.deleteProduct = function(productId, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.DELETE
				+ "?session=" + AuthenticationService.getSession()
				+ "&productId=" + productId;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.products = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.PRODUCTS
				+ "?session=" + AuthenticationService.getSession();
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.orders = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.ORDERS
				+ "?session=" + AuthenticationService.getSession();
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.orderStatus = function(orderId, orderStatus, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.PRODUCT + DefaultConstant.url.ORDER
				+ DefaultConstant.url.STATUS + "?session="
				+ AuthenticationService.getSession() + "&status=" + orderStatus
				+ "&orderId=" + orderId;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	return service;
});

app.factory('UserService', function($http, DefaultConstant,
		AuthenticationService) {
	var service = {};

	service.users = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.USERS
				+ DefaultConstant.url.ALL + "?session="
				+ AuthenticationService.getSession();
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.activate = function(userId, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.USERS
				+ DefaultConstant.url.ACTIVATE + "?session="
				+ AuthenticationService.getSession() + "&userId=" + userId;
		$http.post(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.addPreferenceContact = function(payload, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.PREFERENCE
				+ DefaultConstant.url.ADD + DefaultConstant.url.CONTACT
				+ "?session=" + AuthenticationService.getSession();
		$http.post(url, payload).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	};

	service.preferenceContacts = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.PREFERENCE
				+ DefaultConstant.url.CONTACTS + "?session="
				+ AuthenticationService.getSession();
		$http.get(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	};

	service.preferenceTnc = function(callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.PREFERENCE
				+ DefaultConstant.url.TNC + "?session="
				+ AuthenticationService.getSession();
		$http.get(url).success(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	};

	service.updatePreferenceTnc = function(payload, callback) {
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.PREFERENCE
				+ DefaultConstant.url.UPDATE + DefaultConstant.url.TNC
				+ "?session=" + AuthenticationService.getSession();
		$http.post(url, payload).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	};
	return service;
});