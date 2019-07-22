"use strict";
//User Home Controller
app.controller('UserHomeController',['$scope','$filter','$http','ngDialog',function($scope,$filter,$http,ngDialog){
	//Gets book categories, newly arrived books and most popular books
	$http.get('jspController/Home.jsp').success(function(data){
		$scope.bookCategories = data.categoryList;
		$scope.newlyArrivedBooksList=data.newlyArrivedBooksList;
		$scope.mostPopularBooksList=data.mostPopularBooksList;
	}).error(function(data,status){
		console.log("Error Occured while getting books details for home page. Error Code : "+status);
	});
	
	// Gets User's Recommended Books
	var user=localStorage.getItem("user");
	if(user){
		user=angular.fromJson(user);
		$http.get('jspController/User/RecommendedBooks.jsp',{params: {jsonUser : '{"userEmail":"'+user.userEmail+'"}'}}).success(function(data){
			if(data.result=="SUCCESS"){
				$scope.recommendedBooksList=data.recommendedBooksList;
			} else{
				console.log("Error : "+data.result);
			}
		}).error(function(data,status){
			console.log("Error Occured while getting recommended books for home page. Error Code : "+status);
		});
	}
	
	//Gets Information Data and Advertisement Image Source from Home Page JSON file
	$http.get('js/json/homePageJson.json').success(function(data){
		$scope.information=data.info;
		$scope.advertisementImg=data.advertisementImg;
	}).error(function(data,status){
		console.log("Error Occured while getting information for home page. Error Code : "+status);
	});
	
	/* Show Book Details
	 * Gets book details of selected book
	 */
	$scope.showBookDetails=function(bookNo){
		$scope.bookDetails=$scope.newlyArrivedBooksList.concat($scope.mostPopularBooksList).concat($scope.recommendedBooksList);
		for(var i=0;i<$scope.bookDetails.length;i++){
			if($scope.bookDetails[i].bookNo==bookNo){
				$scope.bookDetails = $scope.bookDetails[i];
				break;
			}
		}
		ngDialog.open({
			template: 'partials/User/homeBooksDialog.html',
			className: 'ngdialog-theme-default',
			scope: $scope,									//Dialog box can access $scope variables of User Home Controller
			controller:'HomeBooksDialogController'
		});
	};
}]);