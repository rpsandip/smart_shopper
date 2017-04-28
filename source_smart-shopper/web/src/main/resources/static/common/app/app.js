/**
 * Application.
 * 
 * @global
 * 
 */
var app = angular.module('web', [ 'ui.router', 'datatables',
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

					$mdThemingProvider.theme('default').primaryPalette('cyan',
							{
								'default' : '700',
								'hue-1' : '700',
								'hue-2' : '800',
								'hue-3' : 'A700'
							}).accentPalette('blue-grey', {
						'default' : 'A200',
						'hue-1' : '500',
						'hue-2' : '700',
						'hue-3' : 'A700'
					});

					$mdThemingProvider.setDefaultTheme('default');
					$mdThemingProvider.alwaysWatchTheme(true);

					$urlRouterProvider.otherwise('/home');

					$stateProvider
							.state(
									'card',
									{
										url : '/home',
										templateUrl : 'components/dashboard/dashboard.card.view.html',
										controller : 'DashboardCardController',
										authenticate : false
									})
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
											'topbarView@dashboard' : {
												templateUrl : 'common/topBar.view.html'
											},
											'sidebarView@dashboard' : {
												templateUrl : 'common/sideBar.view.html'
											},
											'dashboardContentView@dashboard' : {
												templateUrl : 'components/dashboard/dashboard.table.view.html'
											}

										},
										authenticate : false
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
					$state.go('login');
				}
			});
		}
	});
});
