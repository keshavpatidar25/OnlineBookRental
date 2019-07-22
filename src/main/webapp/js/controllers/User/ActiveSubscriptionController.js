"use strict";
//View Active Subscription Plan Controller
app.controller("ActiveSubscriptionController", ['$scope','SubscriptionService', function($scope,SubscriptionService){
	/* Calls Subscription Service get active plan method
	 * Checks whether  user is already subscribed to a plan
	 * If subscribed then display active plan and hides new subscription plan division
	 * Else displays new subscription plan division and hides active subscription plan division
	 */	
	var resultObject = {};
	resultObject=SubscriptionService.getActivePlan();
	$scope.$watch(resultObject,function(){	
		if (resultObject.result == "SUCCESS") {
			angular.element(".newSubscription").hide();
			$scope.plan=resultObject.plan;
			$scope.plan.planStartDate=resultObject.planStartDate;
			$scope.plan.planEndDate=resultObject.planEndDate;
		} else if (resultObject.result == "NOT SUBSCRIBED"){
			angular.element(".activeSubscription").hide();
			angular.element("#msg").addClass("info").removeClass("error").removeClass("success").text("You are currently not subscribed to any plan. Please subscribe from below plans.");	
		} else{
			alert("Error "+resultObject.result);
			console.log("Error "+resultObject.result);
		}
	});
}]);