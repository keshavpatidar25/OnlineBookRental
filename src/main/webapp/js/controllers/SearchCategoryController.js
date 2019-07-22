"use strict";
//Search Category Page Controller
app.controller('SearchCategoryController',['$scope','$http','$filter','$location', '$routeParams','FetchBooksService','SearchSortService', function($scope,$http,$filter,$location,$routeParams,FetchBooksService,SearchSortService){
	$scope.searchString = $routeParams.searchString;

	/* Search by category method of FetchBookService is called in which Search String is passed as a parameter.
	 * FetchBookService provides with list of books.
	 * Then on basis of books available count books availability is set.
	 * Then books list is sorted on basis of most popular.
	 */
	var searchBooks = function(){
		FetchBooksService.bookSearchByCategory($scope.searchString).success(function(data){
			if(data.result=="SUCCESS"){
				$scope.booksList=data.booksList;
				for(var i=0;i<$scope.booksList.length;i++){
					if($scope.booksList[i].bookDetails.bookAvailable>0){
						$scope.booksList[i].bookDetails.bookAvailability="Available";
					}
					else{
						$scope.booksList[i].bookDetails.bookAvailability="Not Available";
					}
				}
				$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
			} else{
				console.log("Error : "+data.result);
			}	
		}).error(function(data,status){
			alert("Error Occured.");
			console.log("Error Occured while searching books by category. Error Code : "+status);
		});
	};
	
	searchBooks();
	
	//Gets Book Category List
	$http.get('jspController/CategoryList.jsp').success(function(data){
		$scope.bookCategories=data.categoryList;
	}).error(function(data,status){
		console.log("Error Occured while getting book category list. Error Code : "+status);
	});
	
	//Called when user changes book category
	$scope.changeCategory=function(newCategory){
		$scope.searchString=newCategory;
		searchBooks();
		$scope.sortBy="Most Popular";
	};
	
	// This method calls SearchSortService to sort books list
	$scope.changeSort = function(){
		if($scope.sortBy=="Most Popular"){
			$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
		} else if($scope.sortBy=="New Arrivals"){
			$scope.booksList=SearchSortService.sortByNewArrival($scope.booksList);
		} 
	};
}]);