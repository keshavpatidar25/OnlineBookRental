"use strict";
//Change Password Controller
app.controller('ChangePasswordController', ['$scope','$http','$filter',function($scope,$http,$filter){
	var user = localStorage.getItem("user");
	$scope.message="";
	$scope.user={};
	if(user){
		user=angular.fromJson(user);
	}
	
	// Get User's Password from database
	$scope.checkOldPassword = function(){
		var oldPassMatched=false;
		angular.element.ajax({
			url :'jspController/GetPassword.jsp',
			data :{jsonUser:'{"userEmail" : "'+user.userEmail+'"}'},
			async : false,
			success : function(result){
				var resultObj=angular.fromJson(result);
				if(resultObj.result=="SUCCESS"){
					if(resultObj.userPass!=$scope.user.oldPass){
						$scope.isErrorMessage=true;
						$scope.message="Wrong Old Password";
					} else{
						oldPassMatched=true;
						$scope.message="";
					}
				} else{
					console.log("Error Occured"+resultObj.result);
				}
			},
			error : function(xhr){
				console.log("An error occured while checking old password : " + xhr.status + " " + xhr.statusText);
			}
		});
		return oldPassMatched;
	};
	
	//Check New Password against confirm Password
	$scope.checkNewPasswords = function(){
		if($scope.user.newPass!=$scope.user.confirmPass){
			$scope.isErrorMessage=true;
			$scope.message="Passwords Donot Match";
		} else{
			$scope.message="";
		}
	};
	
	/* Change Password
	 * Checks Old Password
	 * If match then Checks New Password
	 * If match then Change Password
	 */
	$scope.changePassword = function(){
		if($scope.checkOldPassword()==true){
			$scope.checkNewPasswords();
			if($scope.message==""){
				if(user){
					var userObj = new Object();
					userObj.userEmail=user.userEmail;
					userObj.userPass=$scope.user.newPass;
					$http.get("jspController/User/ChangePassword.jsp",{params:{jsonUser : $filter('json')(userObj)}}).success(function(data){
						if(data.result=="SUCCESS"){
							$scope.message="Password Changed Successfully";
							$scope.isErrorMessage=false;
						} else {
							$scope.message="Details Updation Failed. "+data.message;
							$scope.isErrorMessage=true;
						}
					}).error(function(data,status){
						$scope.message="Error Occured. Please try again.";
						$scope.isErrorMessage=true;
						console.log("Error Occured while changing password. Error Code : "+status);
					});
				}
			}
		}
	};
}]);
