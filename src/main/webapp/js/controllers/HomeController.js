"use strict";

//Controller For Guest Home Page
app.controller('HomeController',['$scope','$filter','$http','ngDialog', function($scope,$filter,$http,ngDialog){
	
	// Gets book categories, newly arrived books list and most popular books list
	$http.get('jspController/Home.jsp').success(function(data){
		$scope.bookCategories = data.categoryList;
		$scope.newlyArrivedBooksList=data.newlyArrivedBooksList;
		$scope.mostPopularBooksList=data.mostPopularBooksList;
	}).error(function(data,status){
		console.log("Error Occured while getting book details for home page. Error Code : "+status);
	});
	
	// Gets information from JSON file
	$http.get('js/json/homePageJson.json').success(function(data){
		$scope.information=data.info;
	}).error(function(data,status){
		console.log("Error Occured while getting information for home page. Error Code : "+status);
	});
	
	// Show book details of newly arrived book and most popular books.
	$scope.showBookDetails=function(bookNo){
		$scope.bookDetails=$scope.newlyArrivedBooksList.concat($scope.mostPopularBooksList);
		for(var i=0;i<$scope.bookDetails.length;i++){
			if($scope.bookDetails[i].bookNo==bookNo){
				$scope.bookDetails = $scope.bookDetails[i];
				break;
			}
		}
		/* Show Details of book in dialog box
		 * @property template : provides html template of dialog box
		 * @property className : dialog box styling class
		 * @property scope : provides scope of dialog box. In this scope : $scope implies that dialog box can access scope variables of parent(home) controller.
		 */
		ngDialog.open({
			template: 'partials/homeBooksDialog.html',
			className: 'ngdialog-theme-default',
			scope: $scope
		});		
	};
}]);