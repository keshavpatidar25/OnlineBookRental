"use strict";
//User Search Controller
app.controller("UserSearchController", ['$scope','$filter','$routeParams','$location','ngDialog','FetchBooksService','SearchSortService','SubscriptionService','BookmarkService','RequestBookService','MailService', function($scope,$filter,$routeParams,$location,ngDialog,FetchBooksService,SearchSortService,SubscriptionService,BookmarkService,RequestBookService,MailService){
	
	$scope.booksFound=true;
	var searchFilter = $routeParams.searchFilter;
	var searchString = $routeParams.searchString;
	
	/* Checks value of search filter. On basis of value of Search Filter method of FetchBookService is called in which Search String is passed as a parameter.
	 * FetchBookService provides with list of books.
	 * Then on basis of books available count books availability is set.
	 * Then books list is sorted on basis of most popular.
	 */
	$scope.booksList=[];
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
	
	/* Get Active Subscription Plan Details Start
	 * Is used to check whether subscription plan is valid or not
	 * Gets Plan End Date which is used to restrict book issue and cancel return request before 7 days of subscription end date
	 * Get No of books allowed to issue by user. If no. of books allowed according to subscription plan is reached then stop issuing book.
	 */
	var activePlanBooksAllowed = null;
	var activePlanResponse = SubscriptionService.getActivePlan();
	$scope.$watch(activePlanResponse,function(){
		if (activePlanResponse.result == "SUCCESS") {
			activePlanBooksAllowed=activePlanResponse.plan.noOfBooksAllowed;
		} else if (activePlanResponse.result == "NOT SUBSCRIBED"){
			activePlanBooksAllowed= -1;	//-1 implies not subscribed
		} else{
			console.log("Error "+activePlanResponse.result);
		}
	});
	//Get Active Subscription Plan Details End
	
	//Book Request Section Start
	var user=localStorage.getItem("user");
	if(user!=null){
		user=angular.fromJson(user);
		$scope.userAddress=user.userAddress;
		$scope.userContact=user.userContact;
	}
	
	/* isIssued is used to display status of book whether it is issued or not.
	 * bookRequestStatus contains the current status of the book whether delivery status is pending, cancelled, etc.
	 * bookRequestStatus  is used in HTML view to hide/show buttons like if delivery status is pending then show "Cancel Issue" button, etc.
	 * issueBookCount is used to count no. of books issued by user. It is used to check against no. of books allowed to issue in subscription plan.
	 */
	$scope.isIssued=[];
	$scope.bookRequestStatus=[];
	var issuedBooksCount = null;
	$scope.issuedBooksServiceResult = FetchBooksService.getIssuedBooks();
	$scope.$watch($scope.issuedBooksServiceResult,function(){
		if($scope.issuedBooksServiceResult.result=="SUCCESS"){
			var issuedBooksList = $scope.issuedBooksServiceResult.issuedBooksList;
			for(var i=0;i<issuedBooksList.length;i++){
				$scope.isIssued[issuedBooksList[i].book.bookNo]=true;
				if(issuedBooksList[i].request.deliveryStatus=="PENDING"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="DELIVERY PENDING";
				} else if(issuedBooksList[i].request.returnStatus=="NOT INITIATED" || issuedBooksList[i].request.returnStatus=="CANCELLED"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="RETURN NI/C";
				} else if(issuedBooksList[i].request.returnStatus=="PENDING"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="RETURN PENDING";
				} 
			}
			issuedBooksCount = issuedBooksList.length;
		} else{
			console.log("Error "+$scope.issuedBooksServiceResult.result);
		}
	});
	
	/* Issue Book Method
	 * Calls RequestBookService issueBook method
	 * Once book is issued, following steps are done :
	 * (i) Entry is done in isIssued[] i.e. isIssued[bookNo]=true;
	 * (ii)bookRequestStatus for that book is set to "DELIVERY PENDING"
	 * (iii)Alert is shown to user
	 * (iv)Mail is sent to user stating book is issued with all the book details.
	 */
	$scope.issueBook = function(bookNoObj){
		var bookNo=bookNoObj.target.attributes.data.value;
		var result = RequestBookService.issueBook(bookNo,$scope.userAddress,$scope.userContact);
		if(result=="SUCCESS"){
			issuedBooksCount++;
			$scope.isIssued[bookNo]=true;
			$scope.bookRequestStatus[bookNo]="DELIVERY PENDING";
			alert("Book Issued Successfully.");
			var issueMail=new Mail();
			issueMail.mailSubject="Book World Book Issue";
			issueMail.mailBody="Dear "+user.userName+",\n\nYour book issue request has been accepted.";
			issueMail.mailTo=user.userEmail;
			MailService.sendMail(issueMail,bookNo);
		} else{
			alert(result);
		}
		$scope.hideConfirmDetailsDialog();
	};
	/* Cancel Issue Book Request Method
	 * Calls RequestBookService cancelIssue method
	 */
	$scope.cancelIssue = function(bookNoObj){
		var bookNo=bookNoObj.target.attributes.data.value;
		var result=RequestBookService.cancelIssue(bookNo);
		if(result=="SUCCESS"){
			issuedBooksCount--;
			$scope.isIssued[bookNo]=false;
			$scope.bookRequestStatus[bookNo]="DELIVERY CANCELLED";
			alert("Book Issue Request Cancelled Successfully.");
			var issueCancelMail=new Mail();
			issueCancelMail.mailSubject="Book World Book Issue Cancelled";
			issueCancelMail.mailBody="Dear "+user.userName+",\n\nYour book issue cancel request has been accepted.";
			issueCancelMail.mailTo=user.userEmail;
			MailService.sendMail(issueCancelMail,bookNo);
		} else{
			alert(result);
		}
	};
	/* Return Book Method
	 * Calls RequestBookService returnBook method
	 */
	$scope.returnBook = function(bookNoObj){
		var bookNo=bookNoObj.target.attributes.data.value;
		var result=RequestBookService.returnBook(bookNo,$scope.userAddress,$scope.userContact);
		if(result=="SUCCESS"){
			$scope.bookRequestStatus[bookNo]="RETURN PENDING";
			alert("Book Return Request Successful.");
			var returnMail=new Mail();
			returnMail.mailSubject="Book World Book Return";
			returnMail.mailBody="Dear "+user.userName+",\n\nYour book return request has been accepted.";
			returnMail.mailTo=user.userEmail;
			MailService.sendMail(returnMail,bookNo);
		} else{
			alert(result);
		}
		$scope.hideConfirmDetailsDialog();
	};
	
	/* Cancel Return Book Request Method
	 * In this first time remaining for subscription plan end is checked.
	 * If it is less than 7 days reject cancel return book request else calls RequestBookService cancelReturn method
	 */
	$scope.cancelReturn = function(bookNoObj){
		if(activePlanResponse.result == "NOT SUBSCRIBED"){
			alert("Book return request cannot be cancelled as you are not subscribed to any subscription plan");
			return;
		}
		var planEndTimeRemaining = Math.ceil((new Date(activePlanResponse.planEndDate).getTime()-new Date().getTime())/(1000 * 3600 * 24));
		if(planEndTimeRemaining<7){
			alert("Book return request cannot be cancelled as your subscription plan will end in less than 7 days");
			return;
		}
		var bookNo=bookNoObj.target.attributes.data.value;
		var result=RequestBookService.cancelReturn(bookNo);
		if(result=="SUCCESS"){
			$scope.bookRequestStatus[bookNo]="RETURN NI/C";		//NI/C implies 'Not Initiated/Cancelled'
			alert("Book Return Request Cancelled Successfully.");
			var returnCancelMail=new Mail();
			returnCancelMail.mailSubject="Book World Book Return Cancelled";
			returnCancelMail.mailBody="Dear "+user.userName+",\n\nYour book return cancel request has been accepted.";
			returnCancelMail.mailTo=user.userEmail;
			MailService.sendMail(returnCancelMail,bookNo);
		} else{
			alert(result);
		}
	};
	
	/* User Details Confirmation Dialog Box Start
	 * Provides user facility to change address and contact no. before placing issue book or return book request.
	 */
	$scope.bookNo={};
	$scope.confirmDetails = function(bookNoObj){
		angular.element("#userDetailsDialog").css("display","block");
		$scope.bookNo = bookNoObj.target.attributes.data.value;
		$scope.showConfirmDetails=true;
	};
	
	$scope.hideConfirmDetailsDialog = function(){
		$scope.showConfirmDetails=false;
	};
	//UserDetails Confirmation Dialog Box End
	
	/* To check whether user is eligible for issuing book or not
	 * This method is called before processing issue book request
	 * It does following :
	 * (i) Checks whether user is subscribed to any plan or not.
	 * (ii) If subscribed, then checks whether user is violating maximum allowed books to issue
	 * (iii) If not violating, then checks time remaining for user's subscription plan to end.
	 * (iv) If it is not less than 7 days then go for issuing books i.e. Confirm User Details.
	 */
	$scope.checkPlanValidity = function(bookNoObj){
		if(activePlanBooksAllowed==-1){
			if(confirm("You are not subscribed to any plan. Click OK to subscribe.")){
				$location.path("/User/subscription");
			}
		} else if(issuedBooksCount>=activePlanBooksAllowed){
			alert("Your book issue limit has reached.");
		} else {
			var planEndTimeRemaining = Math.ceil((new Date(activePlanResponse.planEndDate).getTime()-new Date().getTime())/(1000 * 3600 * 24));
			if(planEndTimeRemaining<7){
				alert("Book cannot be issued as your subscription plan will end in less than 7 days");
			}else{
				$scope.confirmDetails(bookNoObj);	//bookNoObj contains Book No. which will be used in confirmDetails function.
			}
		}
	};
	
	//Book Request Section End
	
	/* Bookmark Section Start
	 * 
	 * isBookmarked is used to show whether book is bookmarked or not
	 */
	$scope.isBookmarked = [];
	
	//Get Bookmarked Books
	$scope.isBookmarked = BookmarkService.getBookmarkedBooks($scope.isBookmarked);
	
	//Add Bookmark
	$scope.addBookmark=function(bookNoObj){
		$scope.isBookmarked = BookmarkService.addBookmark(bookNoObj,$scope.isBookmarked);
	};
	
	// Remove Bookmark 
	$scope.removeBookmark=function(bookNoObj){
		$scope.isBookmarked = BookmarkService.removeBookmark(bookNoObj,$scope.isBookmarked);
	};
	//Bookmark Section End
	
	/*Sorting Section Start
	 *  This method calls SearchSortService to sort books list
	 */
	$scope.changeSort = function(){
		if($scope.sortBy=="Most Popular"){
			$scope.booksList=SearchSortService.sortByMostPopular($scope.booksList);
		} else if($scope.sortBy=="New Arrivals"){
			$scope.booksList=SearchSortService.sortByNewArrival($scope.booksList);
		} else if(($scope.sortBy=="Category Wise")){
			$scope.booksList=SearchSortService.sortByCategory($scope.booksList);
		}
	};
	//Sorting Section End
	
	/* Review And Ratings Section Start
	 * Opens Dialog for rating and review
	 */
	$scope.showRatingsDialog = function(bookNoObj){
		var bookNo = bookNoObj.target.attributes.data.value;
		var bookDetails=null;
		for(var i=0;i<$scope.booksList.length;i++){
			if($scope.booksList[i].bookDetails.bookNo==bookNo){
				bookDetails = $scope.booksList[i].bookDetails;
				break;
			}
		}
		ngDialog.open({
			template: 'partials/User/ratingsDialog.html',
			data: {	'bookDetails': bookDetails},			//data to be sent to dialog box
			className: 'ngdialog-theme-plain',
			controller: 'RatingsDialogController'			//controller of dialog box
		});
	};
	//Review And Ratings Section End
}]);