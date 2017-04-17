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
var routeProvider = app.config([
		'$stateProvider',
		'$urlRouterProvider',
		'$mdThemingProvider',
		'$mdDateLocaleProvider',
		function($stateProvider, $urlRouterProvider, $mdThemingProvider,
				$mdDateLocaleProvider) {

			$mdDateLocaleProvider.formatDate = function(date) {
				return moment(date).format('MMM DD YYYY');
			};

			$mdThemingProvider.theme('default').primaryPalette('cyan', {
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

			$urlRouterProvider.otherwise('/login');

			$stateProvider.state('dashboard', {
				abstract : true,
				url : '/dashboard',
				templateUrl : 'pages/view/dashboard.html',
				controller : 'dashboardController'
			}).state('dashboard.menu', {
				url : '',
				views : {
					'topbarView@dashboard' : {
						templateUrl : 'pages/component/topBar.html'
					}
				}
			}).state('dashboard.item', {
				url : '/item',
				views : {
					'topbarView@dashboard' : {
						templateUrl : 'pages/component/topBar.html'
					},
					'dashboardContentView@dashboard' : {
						templateUrl : 'pages/component/items.html'
					}
				}
			}).state('dashboard.order', {
				url : '/order',
				views : {
					'topbarView@dashboard' : {
						templateUrl : 'pages/component/topBar.html'
					},
					'dashboardContentView@dashboard' : {
						templateUrl : 'pages/component/order.html'
					}
				}
			}).state('dashboard.category', {
				url : '/category',
				views : {
					'topbarView@dashboard' : {
						templateUrl : 'pages/component/topBar.html'
					},
					'dashboardContentView@dashboard' : {
						templateUrl : 'pages/component/categories.html'
					}
				}
			}).state('login', {
				url : '/login',
				templateUrl : 'components/login/login.view.html',
				controller : 'LoginController'
			});
		} ]);

// /**
// * Application directive.
// *
// * NOTE : file drag and drop this event will fire.
// *
// * @global
// */
// app.directive('fileModel', [ '$parse', function(
// $parse) {
// return {
// restrict : 'A',
// link : function(
// scope,
// element,
// attrs) {
// var model = $parse(attrs.fileModel);
// var modelSetter = model.assign;
//
// element.bind('change', function() {
// scope.$apply(function() {
// modelSetter(scope, element[0].files[0]);
// });
// });
// }
// };
// } ]);

/**
 * On Page refresh it triggers event.
 * 
 * @global
 */
// app.run(function(
// $state,
// $rootScope,
// $location,
// $cookieStore,
// $http,
// AuthenticationService) {
// $rootScope.globals = $cookieStore.get('globals') || {};
// if ($rootScope.globals.currentUser) {
// $http.defaults.headers.common['Authorization'] = 'Basic '
// + $rootScope.globals.currentUser.session;
// }
//
// $rootScope.$on('$stateChangeStart', function(
// event,
// toState,
// toParams) {
// AuthenticationService.isLoggedIn(function(
// loggedIn) {
// if (!loggedIn) {
// if (toState.authenticate) {
// event.preventDefault();
// $state.go('login');
// }
// }
// });
// });
// });
