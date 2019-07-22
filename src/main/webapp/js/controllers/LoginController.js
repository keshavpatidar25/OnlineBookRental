"use strict";
//Controller for Login Module
app.controller("LoginController",['$scope','$location','$filter','$routeParams','SubscriptionService', function($scope,$location,$filter,$routeParams,SubscriptionService){
	
	$scope.user = new User();	//Contains user Information
	//Login
	$scope.login = function(){
		var resultObject;
		var jsonUser = $filter('json')($scope.user);
		angular.element.ajax({url: 'jspController/Login.jsp', async: false, type: 'POST', data : {jsonUser : jsonUser}, success : function(data) {
				resultObject = angular.fromJson(data);
				/* if result from Server != "SUCCESS" then show error that came from server.
				 * else store user information in localStorage
				 */
				if(resultObject.result!="SUCCESS"){
					$location.url("login?msg="+resultObject.result+". Please try again.");
				} else{
					if(typeof(Storage) !== "undefined") {
						localStorage.setItem("user",$filter('json')(resultObject.user));
				    } else {
				    	console.log("Sorry, your browser does not support session storage.");
				    }
					/* check User Role
					 * if role is "USER" : 
					 * (i)Check whether user is subscribed to any plan or not.
					 * (ii)If subscribed, redirect to user home
					 * (iii)Else, ask user whether he want to go to subscription page or not. On basis of user's response redirect.
					 * else if role is "OPERATOR" redirect to Modify Request Page 
					 */
					if(resultObject.user.userRole=="USER"){
						if(SubscriptionService.getActivePlan().result=="NOT SUBSCRIBED"){
							if(confirm("You are not subscribed to any plan.\nDo you want to go to Subscription Plan Page."))
							{
								$location.path("User/subscription");
							} else{
								$location.path("User/home");
							}
						} else{
							$location.path("User/home");
						}
					} else{
						$location.path("Operator/modifyRequest");
					}
				}
				
			}, error : function(xhr){
				console.log("An error occured while logging in : " + xhr.status + " " + xhr.statusText);
				$location.url("login?msg=Error Occured. Please try again.");
			}
		});
	};
	/* Checks route parameter "msg"
	 * if msg is defined then display message to user.
	 */
	if($routeParams.msg){
		angular.element("#msg").addClass("error").removeClass("success").text($routeParams.msg);
	}
}]);
