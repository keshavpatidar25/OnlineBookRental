"use strict";

// Controller for Contact Page
app.controller('ContactController',['$scope','$http', '$filter','$location',function($scope,$http,$filter,$location){
	$scope.user=new User();		//contains user details
	$scope.mail=new Mail();		//contains mail information
	
	/* Check if user contact page is opened
	 * If user contact page is openend then check if user details are saved in localStorage.
	 * If saved then retrieve user details and provide them in html input
	 * Else leave html input empty
	 */
	if($location.path()=="/User/contact"){
		if(typeof(Storage) !== "undefined") {
			var userSessionData = angular.fromJson(localStorage.getItem("user"));
			if(userSessionData){	
				$scope.user.userName=userSessionData.userName;
				$scope.user.userEmail=userSessionData.userEmail;
				$scope.user.userContact=userSessionData.userContact;
			}
	    } else {
	    	console.log("Sorry, your browser does not support session storage.");
	    }
	}
	
	/* Composes mail body to be sent.
	 * And makes and ajax request that sends mail.
	 */
	$scope.sendFeedback = function(){
		$scope.mail.mailFromEmail=$scope.user.userEmail;
		$scope.mail.mailBody="Dear Sir/Ma'am,\n\n"+$scope.mailBody+"\n\nThanks,\n"+$scope.user.userName+"\n"+$scope.user.userEmail+"\nContact : "+$scope.user.userContact;
		var jsonMail =  $filter('json')($scope.mail);	//Converts JS Object into JSON String
		angular.element.ajax({
			url: 'jspController/Contact.jsp',
			type: 'GET',
			data: {jsonMail: jsonMail},
			success: function(data){		//Called on successful completion of ajax request
				var resultObject = angular.fromJson(data);		//Converts JSON String into JS Object
				if (resultObject.result == "SUCCESS") {
					angular.element("#msg").addClass("success").removeClass("error").text("Feedback Sent Successfully");
				} else{	
					angular.element("#msg").addClass("error").removeClass("success").text(resultObject.result+" Please try again.");
				}
			},
			error: function(xhr){			//Called when ajax request fails
				angular.element("#msg").addClass("error").removeClass("success").text("Error Occured. Please try again.");
				console.log("An error occured while processing feedback : " + xhr.status + " " + xhr.statusText);
			}			
		});
	};
}]);

