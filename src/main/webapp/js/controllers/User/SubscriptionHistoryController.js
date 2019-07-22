"use strict";
//Subscription Plan History Controller
app.controller("SubscriptionHistoryController", ['$scope','SubscriptionService', function($scope, SubscriptionService){
	// Calls Subscription Service get plan history method
	var resultObject=[];
	resultObject=SubscriptionService.getPlanHistory();
	$scope.$watch(resultObject,function(){	
		if (resultObject.result == "SUCCESS") {
			$scope.planList=resultObject.planList;
		} else{
			alert("Error "+resultObject.result);
			console.log("Error "+resultObject.result);
		}
	});
}]);