"use strict";
//Rating and Review Dialog Controller
app.controller('RatingsDialogController',['$scope','$filter',function($scope,$filter){
	var user= localStorage.getItem("user");
	/* If valid user and bookNo
	 * Then Get Book Details and book Rating Details(Avg Rating, No. of users who have Rated, Rating given by user in past) and Reviews.
	 */
	if(user && $scope.ngDialogData.bookDetails.bookNo){
		user=angular.fromJson(user);
		angular.element.ajax({
			method:'GET',
			url: 'jspController/User/Rating.jsp',
			async: false,
			data: {jsonBook : '{"bookNo" : "'+$scope.ngDialogData.bookDetails.bookNo+'","userEmail" : "'+user.userEmail+'"}'},
			success: function(data){
				var responseObj = angular.fromJson(data);
				if(responseObj.result=="SUCCESS"){
					$scope.bookRating = responseObj.ratingDetails.avgRating;
					$scope.ratingCount = responseObj.ratingDetails.ratingCount;
					$scope.userBookRating = responseObj.userBookRating.bookRating;
					$scope.bookReviewsList = responseObj.bookReviewsList;
					
				} else{
					alert("Error Occured : "+responseObj.result);
					console.log("Error Occured : "+responseObj.result);
				}
			},
			error: function(xhr){
				alert("Error Occured");
				console.log("An error occured while getting book rating and reviews : " + xhr.status + " " + xhr.statusText);
			}
		});
		
		//Store user's initial rating in another variable. It is used in calculating ratings.
		var oldUserBookRating = $scope.userBookRating;
		
		// Submit Rating
		$scope.submitRating=function(){
			var rating=new Object();
			rating.userEmail=user.userEmail;
			rating.bookNo=$scope.ngDialogData.bookDetails.bookNo;
			rating.bookRating=$scope.userBookRating;
			rating = $filter('json')(rating);
			angular.element.ajax({
				method:'GET',
				url: 'jspController/User/AddRating.jsp',
				data: {jsonRating : rating},
				success: function(data){
					var responseObj = angular.fromJson(data);
					if(responseObj.result=="SUCCESS"){
						/* Check if user had rating in past
						 * If not rated then checks if book has been rated in past
						 *    If not then set book rating = user rating
						 * 	  Else set book rating = ((past Book Rating * no of Users who have already rated)+user's Rating)/(no of Users who have already rated + 1))
						 * Increase no. of users who have already rated by 1
						 * Else If user has rated then checks if book has been rated in past
						 * Then set book rating = past Book Rating + ((user's new rating - user's old rating)/no. of user's who have already rated)) 
						 * 
						 * After above operations check in review list whether user has given review in past or not.
						 * If given then change ratings displayed in front of user's review.
						 */
						if(oldUserBookRating==0){
							if($scope.bookRating==0){
								$scope.bookRating=$scope.userBookRating;
							} else{
								$scope.bookRating=(($scope.bookRating*$scope.ratingCount)+$scope.userBookRating)/($scope.ratingCount+1);
							}
							$scope.ratingCount++;
							oldUserBookRating=$scope.userBookRating;
						} else{
							$scope.bookRating= $scope.bookRating + (($scope.userBookRating-oldUserBookRating)/$scope.ratingCount);
							oldUserBookRating=$scope.userBookRating;
						}
						for(var i=0;i<$scope.bookReviewsList.length;i++){
							if($scope.bookReviewsList[i].review.userEmail==user.userEmail){
								$scope.bookReviewsList[i].rating.bookRating = $scope.userBookRating;
							}
						}
						$scope.$apply();
					}else{
						alert("Error Occured : "+responseObj.result);
						console.log("Error Occured : "+responseObj.result);
					}
				},
				error: function(xhr){
					alert("Error Occured");
					console.log("An error occured while submitting rating : " + xhr.status + " " + xhr.statusText);
				}
			});
		
		};
		
		/* Submit Review
		 * It sends ajax request to submit book review
		 * On successful submission of ajax call book review is displayed to user by adding User's Review to book review list.
		 */
		$scope.submitReview=function(){
			var review = new Object();
			review.userEmail=user.userEmail;
			review.bookNo=$scope.ngDialogData.bookDetails.bookNo;
			review.bookReview=$scope.userBookReview;
			var jsonReview = $filter('json')(review);
			angular.element.ajax({
				method:'GET',
				url: 'jspController/User/AddReview.jsp',
				data: {jsonReview : jsonReview},
				success: function(data){
					var responseObj = angular.fromJson(data);
					if(responseObj.result=="SUCCESS"){
						review.reviewDate = new Date();
						var rating = new Object();
						rating.bookRating = $scope.userBookRating;
						$scope.bookReviewsList.push({review : review, rating : rating});
						$scope.$apply();
					}else{
						alert("Error Occured : "+responseObj.result);
						console.log("Error Occured : "+responseObj.result);
					}
				},
				error: function(xhr){
					alert("Error Occured");
					console.log("An error occured while submitting review : " + xhr.status + " " + xhr.statusText);
				}
			});
		};
	}
}]);