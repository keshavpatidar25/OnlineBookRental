"use strict";
//User Search Header Controller
app.controller('UserSearchHeaderController',['$scope','$location','$routeParams', function($scope,$location,$routeParams){
	$scope.searchFilter="All";
	//It displays last search values searched by user in search bar.
	if($routeParams.searchFilter && $routeParams.searchString){
		$scope.searchFilter=$routeParams.searchFilter;
		$scope.searchString=$routeParams.searchString;
	}
	//It routes to user's search book page with search filter and search string as route parameters
	$scope.goSearch = function() {
		$location.url("/User/search/searchFilter/"+$scope.searchFilter+"/searchString/"+$scope.searchString);
	};
	
	trimWhiteSpace();
}]);	