"use strict";

//Controller for Forgot Password Page
app.controller('ForgotPasswordController',['$scope','$http','$filter',function($scope,$http,$filter){
	$scope.user={};							//Contains User Details
	$scope.message="";						//Contains message to be shown to user
	var forgotPasswordMail = new Mail();	//Contains mail details
	forgotPasswordMail.mailSubject="Book World Password Recovery";
	
	/* Get Password of User and Mail it to User's Email
	 * (i) Checks whether user email is valid or not
	 * (ii) If user email is valid. Then gets user's password and mail it to user's email
	 */
	$scope.getPassword=function(){
		forgotPasswordMail.mailToEmail=$scope.user.userEmail;
		/* $http is used to send ajax request. $http returns Promise Object. Promise object have to methods "success" or "failure"
		 * "success" refers to function to be called in case of successful ajax request
		 * "error" refers to function to be called in case of failed ajax request
		 */
		$http.get('jspController/GetPassword.jsp',{params:{jsonUser:'{"userEmail" : "'+$scope.user.userEmail+'"}'}}).success(function(data){
			if(data.result=="SUCCESS"){
				if(data.userPass==""){
					$scope.message="Invalid Email";
					$scope.isErrorMessage=true;
				} else{
					forgotPasswordMail.mailBody="Dear Sir/Ma'am,\n\nYour login credentials of Book World are as follows :\n\nEmail : "+$scope.user.userEmail+"\nPassword : "+data.userPass+"\n\nThanks for being part of our family.\n\nThanks & Regards,\nBook World\nbookworld.care2014@gmail.com\nContact : 1800-18001800";
					angular.element.ajax({
						url:'jspController/ForgotPassword.jsp',
						type:'POST',
						data:{jsonMail:$filter('json')(forgotPasswordMail)},		//$filter('json)(JS Object) Converts JS Object into JSON String
						success:function(result){									//Called on successful completion of ajax request
							var resultObj=angular.fromJson(result);					//Converts JSON String into JS Object
							if(resultObj.result=="SUCCESS"){
								$scope.message="Your credentials have been sent to your Email.";
								$scope.isErrorMessage=false;
							} else{
								$scope.message=resultObj.result;
								$scope.isErrorMessage=true;
							}
						},
						error: function(xhr){										//Called on failure of ajax request
							$scope.message="Error Occured. Please try again.";
							$scope.isErrorMessage=true;
							console.log("An error occured while processing forgot password request : " + xhr.status + " " + xhr.statusText);
						},
						complete: function(){			//Called when ajax request is completed whether success or failure
							$scope.$apply();
						}
					});
				}
			}
			
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while processing forgot password request. Error Code : "+status);
		});
		
	};
}]);