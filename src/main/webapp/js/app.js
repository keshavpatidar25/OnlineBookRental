"use strict";

/*  app is application module
 *  app.config is providing routes configuration
 */

var app = angular.module("OnlineBookRental", [ 'ngRoute','ngDialog','ui.bootstrap', 'ngTable' ]);
app.config([ '$routeProvider', '$provide', '$httpProvider', function($routeProvider, $provide, $httpProvider) {
	//Route Provider
	$routeProvider.when('/', {
		templateUrl : "partials/home.html",
		controller : "HomeController"
	}).when('/login', {
		templateUrl : "partials/login.html"
	}).when('/about', {
		templateUrl : "partials/about.html"
	}).when('/subscription', {
		templateUrl : "partials/subscription.html",
		controller : "SubscriptionController"
	}).when('/contact', {
		templateUrl : "partials/contact.html"
	}).when('/search/searchFilter/:searchFilter/searchString/:searchString', {
		templateUrl : "partials/search.html",
		controller : "SearchController"
	}).when('/searchCategory/searchString/:searchString', {
		templateUrl : "partials/searchCategory.html",
		controller : "SearchCategoryController"
	}).when('/forgotPassword', {
		templateUrl : "partials/forgotPassword.html",
		controller : "ForgotPasswordController"
	}).when('/User/home', {
		templateUrl : "partials/User/home.html",
		controller : "UserHomeController"
	}).when('/User/contact', {
		templateUrl : "partials/User/contact.html",
		controller : "ContactController"
	}).when('/User/subscription', {
		templateUrl : "partials/User/manageSubscription.html",
		controller : "UserSubscriptionController"
	}).when('/User/search/searchFilter/:searchFilter/searchString/:searchString', {
		templateUrl : "partials/User/search.html",
		controller : "UserSearchController"
	}).when('/User/searchCategory/searchString/:searchString', {
		templateUrl : "partials/User/searchCategory.html",
		controller : "UserSearchCategoryController"
	}).when('/User/books', {
		templateUrl : "partials/User/manageBooks.html",
		controller : "UserBookController"
	}).when('/User/updateAccountDetails', {
		templateUrl : "partials/User/updateAccountDetails.html",
		controller : "UpdateAccountDetailsController"
	}).when('/User/changePassword', {
		templateUrl : "partials/User/changePassword.html",
		controller : "ChangePasswordController"
	}).when('/Operator/modifyRequest', {
		templateUrl : "partials/Operator/manageRequests.html",
		controller : "ManageRequestController"
	}).when('/Operator/search/searchFilter/:searchFilter/searchString/:searchString', {
		templateUrl : "partials/Operator/search.html",
		controller : "SearchController"
	}).when('/Operator/books', {
		templateUrl : "partials/Operator/manageBooks.html",
		controller : "OperatorBookController"
	}).when('/Operator/addOperator', {
		templateUrl : "partials/Operator/addOperator.html",
		controller : "AddOperatorController"
	}).when('/Operator/reports', {
		templateUrl : "partials/Operator/reports.html",
		controller : "ReportsController"
	}).when('/logout', {
		templateUrl : "partials/logout.html",
		controller : "LogoutController"
	}).when('/Operator/updateAccountDetails', {
		templateUrl : "partials/Operator/updateAccountDetails.html",
		controller : "UpdateAccountDetailsController"
	}).when('/Operator/changePassword', {
		templateUrl : "partials/Operator/changePassword.html",
		controller : "ChangePasswordController"
	}).otherwise({
		redirectTo : '/'
	});
	
	
/*	 	Intercept http calls.
		Shows Loading Image when  request is being processed*/ 
	  $provide.factory('HttpRequestInterceptor', function ($q,$rootScope) {
	    return {
	      // On request success
	      request: function (config) {
	       // console.log(config); // Contains the data about the request before it is sent.
	    	  if(config.url.search("Header")==-1 && config.url!="jspController/CategoryList.jsp" ){
	    		  $rootScope.httpLoad.showLoadingImg=true;
	    	  }
        // Return the config or wrap it in a promise if blank.
	        return config || $q.when(config);
	      },
	 
	      // On request failure
	      requestError: function (rejection) {
	     //    console.log(rejection); // Contains the data about the error on the request.
	    	  if(rejection.config.url.search("Header")==-1 && rejection.config.url!="jspController/CategoryList.jsp"){
	    		  $rootScope.httpLoad.showLoadingImg=false;
	    	  }
	        // Return the promise rejection.
	        return $q.reject(rejection);
	      },
	 
	      // On response success
	      response: function (response) {
	      //   console.log(response); // Contains the data from the response.
	    	  if(response.config.url.search("Header")==-1 && response.config.url!="jspController/CategoryList.jsp"){
	    		  $rootScope.httpLoad.showLoadingImg=false;
	    	  }
	        // Return the response or promise.
	        return response || $q.when(response);
	      },
	 
	      // On response failture
	      responseError: function (rejection) {
	     //    console.log(rejection); // Contains the data about the error.
	    	  if(rejection.config.url.search("Header")==-1 && rejection.config.url!="jspController/CategoryList.jsp"){
	    		  	$rootScope.httpLoad.showLoadingImg=false;
	    	  }
	        // Return the promise rejection.
	        return $q.reject(rejection);
	      }
	    };
	  });
	 
	  // Add the interceptor to the $httpProvider.
	  $httpProvider.interceptors.push('HttpRequestInterceptor');
	
} ]);

// app.run is triggered whenever web page(index.html) is opened for first time or web page(index.html) is refreshed
app.run(['$rootScope', '$http', function($rootScope, $http) {
	$rootScope.httpLoad={showLoadingImg:false};
	/* Tells what to do on start of route change event.
	 * @param event : route change start
	 * @param next : next route which will open 
	 */
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		var route=next.templateUrl;
		if(!route || (route.search("partials/User")==-1 && route.search("partials/Operator")==-1)){
			return;
		}
		performSessionTracking(route);
	});
}]);
