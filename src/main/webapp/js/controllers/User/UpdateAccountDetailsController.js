"use strict";
//Update Account Details Controller
app.controller('UpdateAccountDetailsController', ['$scope','$http','$filter',function($scope,$http,$filter){
	var user = localStorage.getItem("user");
	$scope.message="";
	$scope.user={};
	if(user!=null){
		user=angular.fromJson(user);
	}
	//Resets the details to initial values present in local storage
	$scope.resetFields=function(){
		if(user){
			$scope.user.userName=user.userName;
			$scope.user.userEmail=user.userEmail;
			$scope.user.userAddress=user.userAddress;
			$scope.user.userContact=user.userContact;
			$scope.message="";
		}
	};
	
	$scope.resetFields();
	
	/* Check Whether User Exists or Not
	 * If user changes its email, then it is checked whether it is already existing or not
	 * If already existing then, restrict user to update details
	 */
	$scope.checkUserExists = function(){
		var responseObj={result:""};
		if(user){
			if(user.userEmail!=$scope.user.userEmail){
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
			}else{
				$scope.message="";
				responseObj.result="SUCCESS";
			}
		}
		return responseObj.result;
	};
		
	/* Update User Details
	 * If details are successfully updated then update details in localStorage
	 */
	$scope.updateDetails=function(){
		
		//Checks User Email Availability
		if($scope.checkUserExists()!="SUCCESS"){
			return;
		}
		if(user){
			var userObj = new Object();
			userObj.userName=$scope.user.userName;
			userObj.userEmail=$scope.user.userEmail;
			userObj.userAddress=$scope.user.userAddress;
			userObj.userContact=$scope.user.userContact;
			userObj.oldUserEmail=user.userEmail;
			$http.get("jspController/User/UpdateAccountDetails.jsp",{params:{jsonUser : $filter('json')(userObj)}}).success(function(data){
				if(data.result=="SUCCESS"){
					localStorage.setItem("user",$filter('json')($scope.user));
					$scope.message="Details Successfully Updated";
					$scope.isErrorMessage=false;
					user=angular.fromJson(localStorage.getItem("user"));
				} else {
					$scope.message="Details Updation Failed. "+data.result;
					$scope.isErrorMessage=true;
				}
			}).error(function(data,status){
				$scope.message="Error Occured. Please try again.";
				$scope.isErrorMessage=true;
				console.log("Error Occured while updating user details. Error Code : "+status);
			});
		}
	};
}]);

