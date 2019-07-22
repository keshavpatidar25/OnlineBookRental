"use strict";
//Currently Held Books Controller
app.controller('CurrentlyHeldBooksController',['$scope','ngDialog','FetchBooksService','BookmarkService','RequestBookService','MailService','SubscriptionService',function($scope,ngDialog,FetchBooksService,BookmarkService,RequestBookService,MailService,SubscriptionService){
	
	/* Get Active Subscription Plan Details Start
	 * Gets Plan End Date which is used to restrict cancel return request before 7 days of subscription end date
	 */
	var activePlanResponse = SubscriptionService.getActivePlan();
	//Get Active Subscription Plan Details End
	
	/* issuedBooksList contains details of all books issued by user.
	 * bookRequestStatus contains the current status of the book whether delivery status is pending, cancelled, etc.
	 * bookRequestStatus  is used in HTML view to hide/show buttons like if delivery status is pending then show "Cancel Issue" button, etc.
	 */
	$scope.issuedBooksList=[];
	$scope.bookRequestStatus=[];
	$scope.issuedBooksServiceResult = FetchBooksService.getIssuedBooksDetails();
	$scope.$watch($scope.issuedBooksServiceResult,function(){
		if($scope.issuedBooksServiceResult.result=="SUCCESS"){
			var issuedBooksList = $scope.issuedBooksServiceResult.issuedBooksList;
			for(var i=0;i<issuedBooksList.length;i++){
				$scope.issuedBooksList.push(issuedBooksList[i].book);
				if(issuedBooksList[i].request.deliveryStatus=="PENDING"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="DELIVERY PENDING";
				} else if(issuedBooksList[i].request.returnStatus=="NOT INITIATED" || issuedBooksList[i].request.returnStatus=="CANCELLED"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="RETURN NI/C";
				} else if(issuedBooksList[i].request.returnStatus=="PENDING"){
					$scope.bookRequestStatus[issuedBooksList[i].book.bookNo]="RETURN PENDING";
				} 
			}
		} else{
			console.log("Error "+$scope.issuedBooksServiceResult.result);
		}
	});
	
	//Book Request Section Start
	var user=localStorage.getItem("user");
	if(user!=null){
		user=angular.fromJson(user);
		$scope.userAddress=user.userAddress;
		$scope.userContact=user.userContact;
	}
	/* Cancel Issue Book Request Method
	 * Calls RequestBookService cancelIssue method
	 * If book cancelled successfully then splice that book from issuedBooksList[] to reflect changes on HTML view.
	 */
	$scope.cancelIssue = function(bookNoObj){
		if(confirm("Are you sure you want to cancel book issue request?")){
			var bookNo=bookNoObj.target.attributes.data.value;
			var result=RequestBookService.cancelIssue(bookNo);
			if(result=="SUCCESS"){
				for(var i=0;i<$scope.issuedBooksList.length;i++){
					if($scope.issuedBooksList[i].bookNo==bookNo){
						$scope.issuedBooksList.splice(i,1);
					}
				}
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
		var planEndTimeRemaining = Math.ceil((new Date(activePlanResponse.planEndDate).getTime()-new Date().getTime())/(1000 * 3600 * 24));
		if(activePlanResponse.result == "NOT SUBSCRIBED"){
			alert("Book return request cannot be cancelled as you are not subscribed to any subscription plan");
			return;
		}
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
	//Book Request Section End
	
	/* User Details Confirmation Dialog Box Start
	 * Provides user facility to change address and contact no. before return book request.
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
	
	/* Review And Ratings Section Start
	 * Opens Dialog for rating and review
	 */
	$scope.showRatingsDialog = function(bookNo){
		var bookDetails=null;
		for(var i=0;i<$scope.issuedBooksList.length;i++){
			if($scope.issuedBooksList[i].bookNo==bookNo){
				bookDetails = $scope.issuedBooksList[i];
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