"use strict";
//Add Operator Controller
app.controller('AddOperatorController',['$scope','$http','$filter',function($scope,$http,$filter){
	//Make user object and set user role to Operator
	$scope.user = new User();
	$scope.user.userRole="OPERATOR";
	$scope.message="";			//Message to be shown to user
	
	//Checks Password and Confirm Password matches or not
	$scope.checkPassword = function(){
		if($scope.user.userPass!=$scope.user.confirmPass) {
			$scope.message="Passwords Donot Match";
			$scope.isErrorMessage=true;
			return false;
		}
		$scope.message="";
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
					$scope.message="User Already Exists";
					$scope.isErrorMessage=true;
				} else if(responseObj.result=="SUCCESS"){
					$scope.message="";
				} else {
					console.log("An error occured while checking user email exists or not : " + responseObj.result);
				}
			},
			error: function(xhr){
				console.log("An error occured while checking user email exists or not : " + xhr.status + " " + xhr.statusText);
			}
		});
		return responseObj.result;
	};
	
	//Registers the Operator
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
				$scope.message="Registration Successful";
				$scope.isErrorMessage=false;
			} else{
				$scope.message=resultObject.result;
				$scope.isErrorMessage=true;
			}
			}, error : function(xhr){
				$scope.message="Error Occured. Please try again.";
				$scope.isErrorMessage=true;
				console.log("An error occured while registering operator : " + xhr.status + " " + xhr.statusText);
			}
		});
	};
}]);