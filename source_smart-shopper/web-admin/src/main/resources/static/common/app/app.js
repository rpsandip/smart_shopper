/**
 * Application.
 * 
 * @global
 * 
 */
var app = angular.module('web-admin', [ 'ui.router', 'datatables',
		'datatables.scroller', 'datatables.bootstrap', 'datatables.buttons',
		'ngResource', 'ngSanitize', 'ngCookies', 'ngMaterial', 'ngMessages' ]);

/**
 * Application Route provider.
 * 
 * @global
 */
var routeProvider = app
		.config([
				'$stateProvider',
				'$urlRouterProvider',
				'$mdThemingProvider',
				'$mdDateLocaleProvider',
				function($stateProvider, $urlRouterProvider,
						$mdThemingProvider, $mdDateLocaleProvider) {

					$mdDateLocaleProvider.formatDate = function(date) {
						return moment(date).format('MMM DD YYYY');
					};

					$mdThemingProvider.theme('default').primaryPalette(
							'blue-grey', {
								'default' : '700',
								'hue-1' : '700',
								'hue-2' : '800',
								'hue-3' : 'A700'
							}).accentPalette('cyan', {
						'default' : 'A200',
						'hue-1' : '500',
						'hue-2' : '700',
						'hue-3' : 'A700'
					});

					$mdThemingProvider.setDefaultTheme('default');
					$mdThemingProvider.alwaysWatchTheme(true);

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
									'dashboard.menu',
									{
										url : '',
										views : {
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.product',
									{
										url : '/product',
										views : {
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											},
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/product/product/products.view.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.order',
									{
										url : '/order',
										views : {
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											},
											'dashboardContentView@dashboard' : {
												templateUrl : 'pages/component/order.html'
											}
										},
										authenticate : true
									})
							.state(
									'dashboard.category',
									{
										url : '/category',
										views : {
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											},
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
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											},
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/user/users.view.html'
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
