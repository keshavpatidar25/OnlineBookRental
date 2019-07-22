"use strict";
//Subscription Service
app.service('SubscriptionService',['$http',function($http){
	//Get Active Plan
	this.getActivePlan = function(){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user!=null){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/ActiveSubscription.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {	
					resultObject = angular.fromJson(data);
				}, error: function(xhr){
					console.log("An error occured while getting active plan using subscription service : " + xhr.status + " " + xhr.statusText);
				}
			});
			return resultObject;
		}
	};
	//Subscribe to new subscription plan
	this.subscribeNewPlan = function(jsonUserPlan){
		var resultObject={};
		angular.element.ajax({url: 'jspController/User/NewSubscription.jsp', type: 'POST', async:false, data : {jsonUserPlan : jsonUserPlan}, 
			success : function(data) {
				resultObject = angular.fromJson(data);
				return resultObject;
			}, error: function(xhr){
				console.log("An error occured while subscribing to new plan using subscription service : " + xhr.status + " " + xhr.statusText);
			}
		});
		return resultObject;
	};
	//Get Subscription History
	this.getPlanHistory = function(){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user!=null){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/SubscriptionHistory.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {
					resultObject = angular.fromJson(data);
					return resultObject;
				}, error: function(xhr){
					console.log("An error occured while getting plan history using subscription service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
	//Get Active Users
	this.getActiveUsers = function(){
		return $http.get('jspController/Operator/GetActiveUsers.jsp');
	};
}]);