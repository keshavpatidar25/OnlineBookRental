"use strict";
//Bookmark Service. 
app.service('BookmarkService', function(){
	
	// Get Bookmarked Books of user. Fetches only book no of bookmarked books
	this.getBookmarkedBooks = function(isBookmarked){
		var user = localStorage.getItem("user");
		if(user){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/GetBookmark.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					if (resultObject.result == "SUCCESS") {
						for(var i=0; i<resultObject.booksList.length;i++){
							isBookmarked[resultObject.booksList[i].bookNo]=true;
						}
					} else{
						console.log("Error "+resultObject.result);
					}
				}, error: function(xhr){
					console.log("An error occured while getting bookmarked books using bookmark service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return isBookmarked;
	};
	
	//Get Bookmarked books details. Fetches all details of bookmarked books
	this.getBookmarkedBooksDetails = function(){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/BookmarkDetails.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {
					resultObject = angular.fromJson(data);
					for(var i=0;i<resultObject.booksList.length;i++){
						if(resultObject.booksList[i].bookAvailable>0){
							resultObject.booksList[i].bookAvailability="Available";
						}
						else{
							resultObject.booksList[i].bookAvailability="Not Available";
						}
					}
				}, error: function(xhr){
					console.log("An error occured while getting bookmarked books details using bookmark service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
	
	//Add Bookmark.
	this.addBookmark=function(bookNoObj,isBookmarked){
		var bookNo=bookNoObj.target.attributes.data.value;
		var user = localStorage.getItem("user");
		if(user){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/AddBookmark.jsp', global: false, async:false, type: 'GET', data : {jsonUserBook : '{"userEmail" : "' + user.userEmail+ '","bookNo" : "'+bookNo+'"}'}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					if (resultObject.result == "SUCCESS") {
						isBookmarked[bookNo]=true;
					} else{
						console.log("Error "+resultObject.result);
					}
				}, error: function(xhr){
					console.log("An error occured while adding bookmark using bookmark service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return isBookmarked;
	};
	
	//Remove Bookmark
	this.removeBookmark=function(bookNoObj,isBookmarked){
		var bookNo=bookNoObj.target.attributes.data.value;
		var user = localStorage.getItem("user");
		if(user){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/RemoveBookmark.jsp', async:false, global: false, type: 'GET', data : {jsonUserBook : '{"userEmail" : "' + user.userEmail+ '","bookNo" : "'+bookNo+'"}'}, 
				success : function(data) {
					var resultObject = angular.fromJson(data);
					if (resultObject.result == "SUCCESS") {
						isBookmarked[bookNo]=false;
					} else{
						console.log("Error "+resultObject.result);
					}
				}, error: function(xhr){
					console.log("An error occured while removing bookmark using bookmark service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return isBookmarked;
	};
});