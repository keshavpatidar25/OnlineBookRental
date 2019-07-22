"use strict";
//Request Book Service
app.service('RequestBookService',['$filter','$http', function($filter,$http){
	//Issue Book
	this.issueBook=function(bookNo, address, contact){
		var hasIssued={};
		var user = localStorage.getItem("user");
		if(user!=null){
			var userObj = angular.fromJson(user);
			user = new User();
			user.userEmail = userObj.userEmail;
			user.userAddress = address;
			user.userContact = contact;
			user.userRole = userObj.userRole;
			var bookRequestObject = new Object();
			bookRequestObject.user=user;
			bookRequestObject.bookNo=bookNo;
			var jsonBookRequest = $filter('json')(bookRequestObject);
			angular.element.ajax({url: 'jspController/User/IssueBook.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					hasIssued=resultObject.result;
				}, error: function(xhr){
					console.log("An error occured while issuing book using request book service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return hasIssued;
	};
	
	//Cancel Issue
	this.cancelIssue = function(bookNo,userEmail){
		var hasIssueCancelled={};
		var user = localStorage.getItem("user");
		if(user!=null){
			var userObj = angular.fromJson(user);
			user=new User();
			if(userObj.userRole=="USER"){
				user.userEmail = userObj.userEmail;
			} else{
				user.userEmail=userEmail;
			}
			user.userRole = userObj.userRole;
			var bookRequestObject = new Object();
			bookRequestObject.user=user;
			bookRequestObject.bookNo=bookNo;
			var jsonBookRequest = $filter('json')(bookRequestObject);
			angular.element.ajax({url: 'jspController/User/CancelIssue.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					hasIssueCancelled=resultObject.result;
				}, error: function(xhr){
					console.log("An error occured while cancelling issue book request using request book service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return hasIssueCancelled;
	};
	
	//Close Issue
	this.closeIssue = function(bookNo,userEmail){
		var hasIssueClosed={};
		var bookRequestObject = new Object();
		bookRequestObject.userEmail=userEmail;;
		bookRequestObject.bookNo=bookNo;
		var jsonBookRequest = $filter('json')(bookRequestObject);
		angular.element.ajax({url: 'jspController/Operator/CloseIssue.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
			success : function(data) {
				var resultObject = angular.fromJson(data);
				hasIssueClosed=resultObject.result;
			}, error: function(xhr){
				console.log("An error occured while closing issue book request using request book service : " + xhr.status + " " + xhr.statusText);
			}
		});
		
		return hasIssueClosed;
	};
	//Return Book
	this.returnBook=function(bookNo, address, contact){
		var hasReturned={};
		var user = localStorage.getItem("user");
		if(user!=null){
			var userObj = angular.fromJson(user);
			user = new User();
			user.userEmail = userObj.userEmail;
			user.userAddress = address;
			user.userContact = contact;
			user.userRole = userObj.userRole;
			var bookRequestObject = new Object();
			bookRequestObject.user=user;
			bookRequestObject.bookNo=bookNo;
			var jsonBookRequest = $filter('json')(bookRequestObject);
			angular.element.ajax({url: 'jspController/User/ReturnBook.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					hasReturned=resultObject.result;
				}, error: function(xhr){
					console.log("An error occured while returning book using request book service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return hasReturned;
	};
	//Cancel Return
	this.cancelReturn = function(bookNo,userEmail){
		var hasReturnCancelled={};
		var user = localStorage.getItem("user");
		if(user!=null){
			var userObj = angular.fromJson(user);
			user=new User();
			if(userObj.userRole=="USER"){
				user.userEmail = userObj.userEmail;
			} else{
				user.userEmail=userEmail;
			}
			user.userRole = userObj.userRole;
			var bookRequestObject = new Object();
			bookRequestObject.user=user;
			bookRequestObject.bookNo=bookNo;
			var jsonBookRequest = $filter('json')(bookRequestObject);
			angular.element.ajax({url: 'jspController/User/CancelReturn.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					hasReturnCancelled=resultObject.result;
				}, error: function(xhr){
					console.log("An error occured while cancelling return book request using request book service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return hasReturnCancelled;
	};
	//Close Return
	this.closeReturn = function(bookNo,userEmail){
		var hasReturnClosed={};
		var bookRequestObject = new Object();
		bookRequestObject.userEmail=userEmail;;
		bookRequestObject.bookNo=bookNo;
		var jsonBookRequest = $filter('json')(bookRequestObject);
		angular.element.ajax({url: 'jspController/Operator/CloseReturn.jsp', async:false, type: 'GET', data : {jsonBookRequest : jsonBookRequest}, 
			success : function(data) {
				var resultObject = angular.fromJson(data);
				hasReturnClosed=resultObject.result;
			}, error: function(xhr){
				console.log("An error occured while closing return book request using request book service : " + xhr.status + " " + xhr.statusText);
			}
		});
		return hasReturnClosed;
	};
	
	//Get Sub Requests
	this.getSubRequests = function(requestNo){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user!=null){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/BooksSubHistory.jsp', async:false, type: 'GET', data : {jsonRequest : '{"requestNo" : "'+requestNo+'"}'}, 
				success : function(data) {	
					resultObject = angular.fromJson(data);
				}, error: function(xhr){
					console.log("An error occured while getting sub requests using request book service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
	//Get All Requests
	this.getAllRequests = function(){
		return $http.get("jspController/Operator/GetAllRequests.jsp");
	};
}]);