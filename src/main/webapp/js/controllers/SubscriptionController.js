"use strict";
//Subscription Page Controller
app.controller("SubscriptionController", ['$scope','$http', function($scope,$http){
	//Get All Subscription Plan Details
	$http.get("jspController/Subscription.jsp").success(function(data){
		$scope.plans=data.planList;
	}).error(function(data,status){
		alert("Error Occured");
		console.log("Error occured while getting subscription plan details. Error Code " + status);
	});
}]);