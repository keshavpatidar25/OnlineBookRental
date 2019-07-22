"use strict";
//Add New Subscription Plan Controller
app.controller("NewSubscriptionController", ['$scope','$filter','$route','SubscriptionService', function($scope,$filter,$route,SubscriptionService){
	
	/* Add New Plan
	 * Calls Subscription Service subscribe new plan method
	 */
	$scope.addNewPlan=function(planObj){ 
		var planId=planObj.target.attributes.data.value;
		if(!confirm("Are you sure you want to subscribe to "+planId+" Plan?")){
			return;
		}
		var user = angular.fromJson(localStorage.getItem("user"));
		var userPlan = new Object();
		userPlan.userEmail=user.userEmail;
		userPlan.planId=planId;
		var jsonUserPlan = $filter('json')(userPlan);
		
		var resultObject = SubscriptionService.subscribeNewPlan(jsonUserPlan);
		$scope.$watch(resultObject,function(){	
			if (resultObject.result == "SUCCESS") {
				alert("Subscription Successful");
				$route.reload();
			} else{
				angular.element("#msg").addClass("error").removeClass("success").removeClass("info").text(resultObject.result+".Please try again.");
				console.log("Error "+resultObject.result);
			}		
		});
	};
}]);