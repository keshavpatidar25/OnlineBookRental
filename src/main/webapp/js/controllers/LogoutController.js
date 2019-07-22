"use strict";
//Controller for Logout Page
app.controller('LogoutController',['$http','$scope',function($http,$scope){
	$scope.successfulLogout=null;
	$scope.userRole=null;
	/* Send ajax request to invalidate HTTP Sesssion.
	 * If session is destroyed successfully clear localStorage
	 */
	$http.get("jspController/Logout.jsp").success(function(data){
		if(data.result=="SUCCESS"){
			$scope.successfulLogout=true;
			localStorage.clear();
		} else{
			$scope.successfulLogout=false;
			$scope.userRole=angular.fromJson(localStorage.getItem("user")).userRole;
		}
	}).error(function(data,status){
		console.log("Error occured while logging out. Error Code : "+status);
	});
}]);