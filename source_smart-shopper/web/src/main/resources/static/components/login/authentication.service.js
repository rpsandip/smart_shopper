'use strict';

app.factory('AuthenticationService', function($http, $cookies, $rootScope,
		$window, $state, $timeout, DefaultConstant, UtilityService) {
	var service = {};

	var cookieKey = 'web-app'

	service.doLogin = function(username, password, callback) {
		var login = {
			"username" : username,
			"password" : password
		};
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.AUTH;
		$http.post(url, login).success(
				function(Response, Status, Headers, Config) {
					callback(Response, Status);
				}).error(function(Response, Status, Headers, Config) {
			callback(Response, Status);
		});
	}

	service.setCredentials = function(session, username, name) {

		var currentUser = new Object();
		currentUser.session = session;
		currentUser.username = username;
		currentUser.name = name;
		
		// store user details in globals cookie that keeps user logged
		// in
		// for 2days (or until they logout)
		$window.localStorage.setItem(cookieKey, JSON.stringify(currentUser));
		// var cookieExp = new Date();
		// cookieExp.setDate(cookieExp.getDate() + 2);
		// $cookies.putObject(cookieKey, $rootScope.globals, {
		// expires : cookieExp
		// });

		$window.location.reload();
	};

	service.doLogout = function() {
		var session = this.getSession();
		var url = DefaultConstant.url.SERVER_ADDRESS
				+ DefaultConstant.url.SUDOERS + DefaultConstant.url.LOG_OUT
				+ "?session=" + session;
		console.log(url)
		$http.post(url).success(function(Response, Status, Headers, Config) {
			$rootScope.globals = {};
			$cookies.remove(cookieKey);
//			$state.go('dashboard.categories')
		}).error(function(Response, Status, Headers, Config) {
			$rootScope.globals = {};
			$cookies.remove(cookieKey);
//			$state.go('dashboard.categories')
		});
	};

	service.getSession = function() {
		var session = angular.fromJson($window.localStorage.getItem(cookieKey));
		if (session) {
			return session.session;
		}
	};

	service.getUsername = function() {
		var session = angular.fromJson($window.localStorage.getItem(cookieKey));
		if (session) {
			return session.username;
		}
	};

	service.getName = function() {
		var session = angular.fromJson($window.localStorage.getItem(cookieKey));
		if (session) {
			return session.name;
		}
	};

	service.isLoggedIn = function(callback) {
		var session = angular.fromJson($window.localStorage.getItem(cookieKey));
		if (session) {
			var currentUserSession = session.session;
			var url = DefaultConstant.url.SERVER_ADDRESS
					+ DefaultConstant.url.SUDOERS
					+ DefaultConstant.url.IS_ACTIVE + "?session="
					+ currentUserSession;
			$http.post(url).success(
					function(Response, Status, Headers, Config) {
						callback(true, Response);
					}).error(function(Response, Status, Headers, Config) {
				callback(false, Response);
			});
		} else {
			callback(false);
		}
	};

	service.clearCredentials = function() {
		$rootScope.globals = {};
		$window.localStorage.removeItem(cookieKey);
	}
	return service;
});
