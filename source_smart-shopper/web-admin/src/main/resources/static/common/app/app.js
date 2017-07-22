/**
 * Application.
 * 
 * @global
 * 
 */
var app = angular.module('web-admin', [ 'ui.router', 'datatables',
		'datatables.buttons', 'ngResource', 'ngSanitize', 'ngCookies',
		'ngMessages', 'ui.tree' ]);

/**
 * Application Route provider.
 * 
 * @global
 */
var routeProvider = app
		.config([
				'$stateProvider',
				'$urlRouterProvider',
				function($stateProvider, $urlRouterProvider) {

					$urlRouterProvider.otherwise('/login');

					$stateProvider
							.state(
									'dashboard',
									{
										abstract : true,
										url : '',
										templateUrl : 'components/dashboard/dashboard.view.html',
										controller : 'DashboardController',
										authenticate : true
									})
							.state(
									'dashboard.product',
									{
										url : '/product',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/product/product/products.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.category',
									{
										url : '/category',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/product/categories.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.user',
									{
										url : '/user',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/user/users.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.order',
									{
										url : '/order',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/product/order.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.preference',
									{
										url : '/preference',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/preference/preference.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'login',
									{
										url : '/login',
										templateUrl : 'components/login/login.view.html',
										controller : 'LoginController',
										authenticate : false
									});
				} ]);

/**
 * On Page refresh it triggers event.
 * 
 * @global
 */
app.run(function($state, $rootScope, $location, $cookieStore, $http,
		AuthenticationService) {
	$rootScope.globals = $cookieStore.get('web-admin-app') || {};
	if ($rootScope.globals.currentUser) {
		$http.defaults.headers.common['Authorization'] = 'Basic '
				+ $rootScope.globals.currentUser.session;
	}

	$rootScope.$on('$stateChangeStart', function(event, toState, toParams) {
		if (toState.authenticate === true) {
			AuthenticationService.isLoggedIn(function(response) {
				if (!response) {
					event.preventDefault();
					$state.go('login');
				}
			});
		}
	});
});
