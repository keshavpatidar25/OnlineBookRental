"use strict";
//Controller For User Registration Module
app.controller("RegisterController",['$scope','$location','$filter','$http', function($scope,$location,$filter,$http){
	$scope.user = new User();
	$scope.user.userRole="USER";	//Set user role to user
	
	//Checks Password and Confirm Password matches or not
	$scope.checkPassword = function(){
		if($scope.user.userPass!=$scope.user.confirmPass) {
			angular.element("#msg").addClass("error").removeClass("success").text("Passwords donot match.");
			return false;
		}
		angular.element("#msg").removeClass("error").removeClass("success").text("");
		return true;
	};
	
	//Checks whether user email already exists or is available for registration
	$scope.checkEmail = function(){
		var responseObj={result:""};
		angular.element.ajax({url : "jspController/User/CheckUserExists.jsp",
			data:{jsonUser : '{"userEmail":"'+$scope.user.userEmail+'"}'},
			async : false,
			success : function(data){
				responseObj=angular.fromJson(data);
				if(responseObj.result=="User Already Exists"){
					angular.element("#msg").addClass("error").removeClass("success").text("User Already Exists. Please try again.");
				} else if(responseObj.result=="SUCCESS"){
					angular.element("#msg").removeClass("error").removeClass("success").text("");
				} else {
					console.log(responseObj.result);
				}
			},
			error: function(xhr){
				console.log("An error occured while checking user email exists or not : " + xhr.status + " " + xhr.statusText);
			}
		});
		return responseObj.result;
	};
	
	//Registers the User
	$scope.register = function(){
		//Check Password and Confirm Password
		if(!$scope.checkPassword()){
			return;
		}
		//Checks User Email Availability
		if($scope.checkEmail()!="SUCCESS"){	   
			return;
		}
		//User Address is undefined or null then set user address to empty string
		if(!($scope.user.userAddress)){		  
			$scope.user.userAddress="";
		} 
		//User Contact is undefined or null then set user contact to empty string
		if(!($scope.user.userContact)){		  
			$scope.user.userContact="";
		}
		var jsonUser =  $filter('json')($scope.user);
		angular.element.ajax({url: 'jspController/Register.jsp', async: false, type: 'POST', data : {jsonUser : jsonUser}, success : function(data) {
			var resultObject = angular.fromJson(data);
			//Displays Successful message when "SUCCESS" comes from server else displays error message
			if (resultObject.result == "SUCCESS") {
				angular.element("#msg").addClass("success").removeClass("error").text("Registration Successful");
			} else{	
				angular.element("#msg").addClass("error").removeClass("success").text(resultObject.result+". Please try again.");
			}
			}, error : function(xhr){	//Called when ajax request fails. Displays error message
				angular.element("#msg").addClass("error").removeClass("success").text("Error Occured. Please try again.");
				console.log("An error occured while user registration : " + xhr.status + " " + xhr.statusText);
			}
		});
	};
}]);
