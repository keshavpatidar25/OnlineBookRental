"use strict";

//Book History Controller
app.controller('BooksHistoryController',['$scope','FetchBooksService','RequestBookService',function($scope,FetchBooksService,RequestBookService){
	$scope.booksHistoryList={};
	$scope.isFirstHistory=true;
	
	/* Get Book History
	 * Checks validity of from Date and to Date
	 * If valid then get book history between from and to dates
	 */
	$scope.getBooksHistory = function(){
		$scope.isFirstHistory=false;
		if($scope.fromDate && $scope.toDate){
			var booksHistoryServiceResult = FetchBooksService.getBooksHistory($scope.fromDate,$scope.toDate);
			if(booksHistoryServiceResult.result=="SUCCESS"||booksHistoryServiceResult.result=="From Date cannot be greater than to Date"){
				$scope.booksHistoryList= booksHistoryServiceResult.booksHistoryList;
			} else{
				console.log("Book History Cannot be fetched. Error : "+booksHistoryServiceResult.result);
			}
		}
	};
	
	// Get Sub Requests
	var getSubRequests = function(){
		var subRequestServiceResult = RequestBookService.getSubRequests($scope.requestNo);
		if(subRequestServiceResult.result=="SUCCESS"){
			$scope.subRequestsList=subRequestServiceResult.subRequestsList;
		} else{
			console.log("Book History Cannot be fetched. Error : "+booksHistoryServiceResult.result);
		}
	};
	
	/*Sub Requests Dialog Box Start
	 * Initially Sub Request Dialog Box is hidden.
	 */	
	//Shows Sub Request Dialog Box and calls getSubRequests() Method.
	$scope.displaySubRequests = function(requestNo){
		angular.element("#subRequestsDialog").css("display","block");
		$scope.showSubRequests=true;
		$scope.requestNo=requestNo;
		getSubRequests();
	};
	
	//Hides Sub Request Dialog Box. It is done when clicked on close icon on top right corner of Dialog Box.
	$scope.hideSubRequests = function(){
		$scope.showSubRequests=false;
	};
	//Sub Requests Dialog Box End
}]);