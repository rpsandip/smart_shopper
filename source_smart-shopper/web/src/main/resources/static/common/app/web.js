/**
 * Application.
 * 
 * @global
 * 
 */
var app = angular.module('web', [ 'ui.router', 'datatables',
		'datatables.buttons', 'ngResource', 'ngSanitize', 'ngCookies',
		'ui.bootstrap', 'ngMessages', 'mgo-angular-wizard' ]);

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

					$urlRouterProvider.otherwise('/home');

					$stateProvider
							.state(
									'dashboard',
									{
										abstract : true,
										url : '',
										templateUrl : 'components/dashboard/dashboard.view.html',
										controller : 'DashboardController',
										authenticate : false
									})
							.state(
									'dashboard.categories',
									{
										url : '/categories',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/dashboard/dashboard.table.view.html'
											}
										},
										authenticate : false
									})
							.state(
									'dashboard.orders',
									{
										url : '/orders',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/product/order.view.html'
											}

										},
										authenticate : true
									})
							.state(
									'dashboard.home',
									{
										url : '/home',
										views : {
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/dashboard/dashboard.card.view.html'
											}

										},
										authenticate : false
									})
				} ]);

/**
 * On Page refresh it triggers event.
 * 
 * @global
 */
app.run(function($state, $rootScope, $location, $cookieStore, $http,
		AuthenticationService) {
	$rootScope.globals = $cookieStore.get('web-app') || {};
	if ($rootScope.globals.currentUser) {
		$http.defaults.headers.common['Authorization'] = 'Basic '
				+ $rootScope.globals.currentUser.session;
	}

	$rootScope.$on('$stateChangeStart', function(event, toState, toParams) {
		if (toState.authenticate === true) {
			AuthenticationService.isLoggedIn(function(response) {
				if (!response) {
					event.preventDefault();
					$state.go('dashboard.home');
				}
			});
		}
	});
});
