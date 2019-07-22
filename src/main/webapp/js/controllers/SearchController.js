"use strict";
//Search Page Controller
app.controller('SearchController',['$scope','$filter','$location', '$routeParams','ngDialog','FetchBooksService','SearchSortService', function($scope,$filter,$location,$routeParams,ngDialog,FetchBooksService,SearchSortService){
	
	$scope.booksFound=true;
	var searchFilter = $routeParams.searchFilter;
	var searchString = $routeParams.searchString;
		
	$scope.booksList=[];
	/* Checks value of search filter. On basis of value of Search Filter method of FetchBookService is called in which Search String is passed as a parameter.
	 * FetchBookService provides with list of books.
	 * Then on basis of books available count books availability is set.
	 * Then books list is sorted on basis of most popular.
	 */
	if(searchFilter=="All"){
		FetchBooksService.bookSearchByAll(searchString).success(function(data){
			if(data.result=="SUCCESS"){
				$scope.booksList=data.booksList;
				setBooksAvailability();
				$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
				if($scope.booksList.length==0){
					$scope.booksFound=false;
				}
			} else{
				console.log("Error : "+data.result);
			}
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while searching books by all. Error Code : "+status);
		});
	} else if(searchFilter=="Title"){
		FetchBooksService.bookSearchByTitle(searchString).success(function(data){
			if(data.result=="SUCCESS"){
				$scope.booksList=data.booksList;
				setBooksAvailability();
				$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
				if($scope.booksList.length==0){
					$scope.booksFound=false;
				}
			} else{
				console.log("Error : "+data.result);
			}
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while searching books by title. Error Code : "+status);
		});
	} else if(searchFilter=="Author"){
		FetchBooksService.bookSearchByAuthor(searchString).success(function(data){
			if(data.result=="SUCCESS"){	
				$scope.booksList=data.booksList;
				setBooksAvailability();
				$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
				if($scope.booksList.length==0){
					$scope.booksFound=false;
				}
			} else{
				console.log("Error : "+data.result);
			}
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while searching books by author. Error Code : "+status);
		});
	} else if(searchFilter=="Category"){
		FetchBooksService.bookSearchByCategory(searchString).success(function(data){
			if(data.result=="SUCCESS"){
				$scope.booksList=data.booksList;
				setBooksAvailability();
				$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
				if($scope.booksList.length==0){
					$scope.booksFound=false;
				}
			} else{
				console.log("Error : "+data.result);
			}
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while searching books by category. Error Code : "+status);
		});
	}
	
	// Set Book Availability sets whether book is available or not available on basis of book available count
	var setBooksAvailability = function(){
		for(var i=0;i<$scope.booksList.length;i++){
			if($scope.booksList[i].bookDetails.bookAvailable>0){
				$scope.booksList[i].bookDetails.bookAvailability="Available";
			}
			else{
				$scope.booksList[i].bookDetails.bookAvailability="Not Available";
			}
		}	
	};
	
	// This method calls SearchSortService to sort books list
	$scope.changeSort = function(){
		if($scope.sortBy=="Most Popular"){
			$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
		} else if($scope.sortBy=="New Arrivals"){
			$scope.booksList=SearchSortService.sortByNewArrival($scope.booksList);
		} else if(($scope.sortBy=="Category Wise")){
			$scope.booksList=SearchSortService.sortByCategory($scope.booksList);
		}
	};
	
	/* This method is of used in case when Operator searches for book.
	 * This is method for opening dialog box which provides facility to update and delete book.
	 */
	$scope.modifyBook = function(bookNo){
		var i=null;
		$scope.bookToModify=null;
		for(i=0;i<$scope.booksList.length;i++){
			if($scope.booksList[i].bookDetails.bookNo==bookNo){
				$scope.bookToModify=angular.fromJson($filter('json')($scope.booksList[i].bookDetails));
				break;
			}
		}
		ngDialog.open({
			template: 'partials/Operator/modifyBookDialog.html',
			className: 'ngdialog-theme-default',
			data:{'modifiedBookIndex':i},				//data(index of book to be modified in $scope.booksList) sent to Dialog Box
			scope: $scope,								//can access scope variables of SearchController from Dialog Box
			controller: 'ModifyBookDialogController'	//controller of Dialog Box
		});
	};
}]);	