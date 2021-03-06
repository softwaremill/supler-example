"use strict";

angular.module('smlBootzooka.common.directives', []);
angular.module('smlBootzooka.common.filters', []);
angular.module('smlBootzooka.common.services', []);
angular.module('smlBootzooka.common', ['smlBootzooka.common.filters', 'smlBootzooka.common.directives', 'smlBootzooka.common.services']);
angular.module('smlBootzooka.notifications', []);

angular.module('smlBootzooka.profile', ['ui.router', 'smlBootzooka.session', 'smlBootzooka.common', 'smlBootzooka.notifications'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.when('', '/');

        $stateProvider
            .state('login', {
                url: '/login',
                controller: 'LoginCtrl',
                templateUrl: "profile/login/login.html",
                params: {
                    page: null
                }
            })
            .state('supler', {
                url: '/supler?id',
                controller: 'SuplerCtrl',
                templateUrl: "profile/supler/supler.html"
            })
            .state('supler-list', {
                url: '/supler-list',
                controller: 'SuplerListCtrl',
                templateUrl: "profile/supler/supler-list.html"
            })
            .state('terror', {
                url: '/terror',
                controller: 'TerrorCtrl',
                templateUrl: "profile/supler/terror.html"
            })
            .state('register', {
                url: '/register',
                controller: 'RegisterCtrl',
                templateUrl: "profile/register/register.html"
            })
            .state('recover-lost-password', {
                url: '/recover-lost-password',
                controller: 'PasswordRecoveryCtrl',
                templateUrl: "profile/password/recover-lost-password.html"
            })
            .state('password-reset', {
                url: '/password-reset?code',
                controller: "PasswordRecoveryCtrl",
                templateUrl: "profile/password/password-reset.html"
            })
            .state('profile', {
                url: '/profile',
                controller: "ProfileCtrl",
                templateUrl: "profile/profile/profile.html",
                data: {
                    auth: true
                }
            });
    });

angular.module('smlBootzooka.session', ['ngCookies', 'ngResource']);

angular.module(
        'smlBootzooka', [
            'smlBootzooka.templates',
            'smlBootzooka.profile',
            'smlBootzooka.session',
            'smlBootzooka.common',
            'smlBootzooka.notifications', 'ngSanitize', 'ui.router'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/error404');

        $stateProvider
            .state('error404', {
                url: '/error404',
                templateUrl: 'common/errorpages/error404.html'
            })
            .state('main', {
                url: '/main',
                templateUrl: "common/private.html",
                data: {
                    auth: true
                }
            })
            .state('home', {
                url: '/',
                templateUrl: "common/public.html"
            });
    })
    .config(['$httpProvider', function ($httpProvider) {
        var interceptor = ['$q', 'FlashService', '$injector', 'NotificationsService', function ($q, FlashService, $injector, NotificationsService) {

            function redirectToState(stateName) {
                // Because $httpProvider is a factory for $http which is used by $state we can't inject it directly
                // (this way we will get circular dependency error).
                // Using $injector.get will lead to having two instances of $http in our app.
                // By calling $injector.invoke we can delay injection to the moment when application is up & running,
                // therefore we will be injecting existing (and properly configured) $http instance.
                $injector.invoke(function ($state) {
                    $state.go(stateName);
                });
            }

            function success(response) {
                return response;
            }

            function error(response) {
                if (response.status === 401) { // user is not logged in
                    var UserSessionService = $injector.get('UserSessionService'); // uses $injector to avoid circular dependency
                    if (UserSessionService.isLogged()) {
                        UserSessionService.logout(); // Http session expired / logged out - logout on Angular layer
                        FlashService.set('Your session timed out. Please login again.');
                        redirectToState('login');
                    }
                } else if (response.status === 403) {
                    console.log(response.data);
                    // do nothing, user is trying to modify data without privileges
                } else if (response.status === 404) {
                    redirectToState('error404');
                } else {
                    NotificationsService.showError('Something went wrong..', 'Unexpected error');
                }
                return $q.reject(response);
            }

            return {
                response : success,
                responseError : error
            };

        }];
        $httpProvider.interceptors.push(interceptor);
    }])
    .run(function ($rootScope, UserSessionService, $state) {

        function requireAuth(targetState) {
            return targetState && targetState.data && targetState.data.auth;
        }

        $rootScope.$on('$stateChangeStart', function(ev, targetState) {
            if(requireAuth(targetState) && UserSessionService.isNotLogged()) {
                $state.go('login', { page: targetState.url });
                ev.preventDefault();
            }
        });
    })
    .run(function ($rootScope, $timeout, FlashService, NotificationsService) {
        $rootScope.$on("$stateChangeSuccess", function () {
            var message = FlashService.get();
            NotificationsService.showInfo(message);
        });
    });
