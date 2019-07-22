"use strict";
//Home Books(New Arrival, Most Popular and Recommended Books) Dialog Box Controller
app.controller('HomeBooksDialogController',['$scope','ngDialog','BookmarkService',function($scope,ngDialog,BookmarkService){
	//Bookmark Section Start
	$scope.isBookmarked = [];
	//Gets bookmarked books
	$scope.isBookmarked = BookmarkService.getBookmarkedBooks($scope.isBookmarked);
	
	//Adds bookmark
	$scope.addBookmark=function(bookNoObj){
		$scope.isBookmarked = BookmarkService.addBookmark(bookNoObj,$scope.isBookmarked);
	};
	
	//Remove bookmark
	$scope.removeBookmark=function(bookNoObj){
		$scope.isBookmarked = BookmarkService.removeBookmark(bookNoObj,$scope.isBookmarked);
	};
	//Bookmark Section End
	
	//Review And Ratings Section Start. Opens review and rating dialog box
	$scope.showRatingsDialog = function(){
		ngDialog.open({
			template: 'partials/User/ratingsDialog.html',
			data: {	'bookDetails': $scope.bookDetails},		//Book Details are sent to Dialog Box
			className: 'ngdialog-theme-plain',
			controller: 'RatingsDialogController'
		});
	};
	//Review And Ratings Section End
}]);