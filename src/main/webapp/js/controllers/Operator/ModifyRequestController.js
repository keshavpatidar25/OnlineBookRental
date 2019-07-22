"use strict";
//Modify Request Controller
app.controller('ModifyRequestController',['$scope','$filter','$q','ngTableParams','RequestBookService','MailService',function($scope,$filter,$q,ngTableParams,RequestBookService,MailService){
	$scope.requestsList=[];
	$scope.reqDelivery=[];
	$scope.reqReturn=[];
	$scope.requestIndex=[];
	$scope.showRequestCancelDialog=false;
	RequestBookService.getAllRequests().success(function(data){
		$scope.requestsList = data.requestsList;
		for(var i=0;i<$scope.requestsList.length;i++){
			$scope.reqDelivery[$scope.requestsList[i].request.requestNo]=$scope.requestsList[i].request.deliveryStatus;
			$scope.reqReturn[$scope.requestsList[i].request.requestNo]=$scope.requestsList[i].request.returnStatus;
			$scope.requestIndex[$scope.requestsList[i].request.requestNo]=i;
		}
		//Ng-Table Section Start
		$scope.modifyRequestTable = new ngTableParams({
	        page: 1,            // show first page
	        count: 10,          // count per page
	        filter: {
				requestNo:'',
				userEmail:'',
				bookNo:'',
				bookTitle:'',
				deliveryStatus:'',
				returnStatus:''
			},
			sorting: {
				'request.requestNo':'desc'
			}
			
	    }, {
	    	//Enable Sorting Feature of Ng-Table
	        total: $scope.requestsList.length, // length of data
	        getData: function($defer, params) {
	        	var orderedData = params.sorting() ?
                        $filter('orderBy')($scope.requestsList, params.orderBy()) :
                        $scope.requestsList;
            //Enable Filter Feature of Ng-Table
                var orderedData =$filter('filter')(
                					$filter('filter')(
                						$filter('filter')(
                							$filter('filter')(
                								$filter('filter')(
                									$filter('filter')(orderedData,
                									{request:{requestNo:params.filter().requestNo}}),
                								{request:{userEmail:params.filter().userEmail}}),
                							{request:{deliveryStatus:params.filter().deliveryStatus}}),
                						{request:{returnStatus:params.filter().returnStatus}}), 
                					{book:{bookNo:params.filter().bookNo}}),
                				{book:{bookTitle:params.filter().bookTitle}});
	            
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	        }
	    });
		//Ng-Table Section End
	}).error(function(data,status){
		alert("Error Occured.");
		console.log("Error Occured while deleting book. Error Code : "+status);
	});
	//Gives Combo Box Filter in Delivery Request Status column of active user's table
	$scope.deliveryComboBox = function() {
        var def = $q.defer(),
        status = [{id:'',title:'All'},{id:'Pending',title:'Pending'},{id:'Cancelled',title:'Cancelled'},{id:'Closed',title:'Closed'}];
        def.resolve(status);
        return def;
    };
    
  //Gives Combo Box Filter in Return Request Status column of active user's table
    $scope.returnComboBox = function() {
        var def = $q.defer(),
        status = [{id:'',title:'All'},{id:'Not Initiated',title:'Not Initiated'},{id:'Pending',title:'Pending'},{id:'Cancelled',title:'Cancelled'},{id:'Closed',title:'Closed'}];
        def.resolve(status);
        return def;
    }; 
    /* Resets the delivery and return request status column value related to particular requestNo
     * @param requestNo : request no whose delivery and return request status column value is to be reset
     */
    $scope.resetChanges = function(requestNo){
    	$scope.reqDelivery[requestNo]=$scope.requestsList[$scope.requestIndex[requestNo]].request.deliveryStatus;
		$scope.reqReturn[requestNo]=$scope.requestsList[$scope.requestIndex[requestNo]].request.returnStatus;
    };
    
    /* Update Changes in Request Status
     * Checks which status has changed(Delivery or Return) and what change has been done(like Pending to Cancelled or Pending to Closed).
     * Call appropriate method on basis of that.
     */
    $scope.updateChanges=function(requestNo){
    	if($scope.requestsList[$scope.requestIndex[requestNo]].request.deliveryStatus=="PENDING"){
    		if($scope.reqDelivery[requestNo]=='CANCELLED'){
    			cancelIssue(requestNo);
    		} else if($scope.reqDelivery[requestNo]=='CLOSED'){
    			closeIssue(requestNo);
    		} else{
    			alert("No changes detected in request Status. Kindly change request status and try again");
    		}
    	} else{
    		if($scope.reqReturn[requestNo]=='CANCELLED'){
    			cancelReturn(requestNo);
    		} else if($scope.reqReturn[requestNo]=='CLOSED'){
    			closeReturn(requestNo);
    		} else{
    			alert("No changes detected in request Status. Kindly change request status and try again");
    		}
    	}
    };
    
    /* Cancel Issue
     * Calls Request Book Service Cancel Issue Method
     * When successful, Send user mail.
     */
    var cancelIssue = function(requestNo){
		var result=RequestBookService.cancelIssue($scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo,$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail);
		if(result=="SUCCESS"){
			$scope.requestsList[$scope.requestIndex[requestNo]].request.deliveryStatus="CANCELLED";
			alert("Book Issue Request Cancelled Successfully.");
			var issueCancelMail=new Mail();
			issueCancelMail.mailSubject="Book World Book Issue Cancelled";
			issueCancelMail.mailBody="Dear Sir/Ma'am,\n\nYour book issue request has been cancelled by our operator due to following reason:\n\nReason : "+$scope.cancelReason;
			issueCancelMail.mailTo=$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail;
			MailService.sendMail(issueCancelMail,$scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo);
		} else{
			alert(result);
		}
	};
	
	/* Close Issue
     * Calls Request Book Service Close Issue Method
     * When successful, Send user mail.
     */
	var closeIssue = function(requestNo){
		var result=RequestBookService.closeIssue($scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo,$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail);
		if(result=="SUCCESS"){
			$scope.requestsList[$scope.requestIndex[requestNo]].request.deliveryStatus="CLOSED";
			alert("Book Issue Request Closed Successfully.");
			var issueCloseMail=new Mail();
			issueCloseMail.mailSubject="Book World Book Issue Request Closed";
			issueCloseMail.mailBody="Dear Sir/Ma'am,\n\nYour book issue request has been closed";
			issueCloseMail.mailTo=$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail;
			MailService.sendMail(issueCloseMail,$scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo);
		} else{
			alert(result);
		}
	};
	
	/* Cancel Return
     * Calls Request Book Service Cancel Return Method
     * When successful, Send user mail.
     */
	var cancelReturn = function(requestNo){
		var result=RequestBookService.cancelReturn($scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo,$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail);
		if(result=="SUCCESS"){
			$scope.requestsList[$scope.requestIndex[requestNo]].request.returnStatus="CANCELLED";
			alert("Book Return Request Cancelled Successfully.");
			var returnCancelMail=new Mail();
			returnCancelMail.mailSubject="Book World Book Return Cancelled";
			returnCancelMail.mailBody="Dear Sir/Ma'am,\n\nYour book return request has been cancelled by our operator due to following reason:\n\nReason : "+$scope.cancelReason;
			returnCancelMail.mailTo=$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail;
			MailService.sendMail(returnCancelMail,$scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo);
		} else{
			alert(result);
		}
	};
	
	/* Close Return
     * Calls Request Book Service Close Return Method
     * When successful, Send user mail.
     */
	var closeReturn = function(requestNo){
		var result=RequestBookService.closeReturn($scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo,$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail);
		if(result=="SUCCESS"){
			$scope.requestsList[$scope.requestIndex[requestNo]].request.returnStatus="CLOSED";
			alert("Book Return Request Closed Successfully.");
			var returnCloseMail=new Mail();
			returnCloseMail.mailSubject="Book World Book Return Request Closed";
			returnCloseMail.mailBody="Dear Sir/Ma'am,\n\nYour book return request has been closed";
			returnCloseMail.mailTo=$scope.requestsList[$scope.requestIndex[requestNo]].request.userEmail;
			MailService.sendMail(returnCloseMail,$scope.requestsList[$scope.requestIndex[requestNo]].book.bookNo);
		} else{
			alert(result);
		}
	};
	
	//Hide Dialog Box which is shown when operator tries to cancel issue request or return request of user.
	$scope.hideRequestCancelDialog=function(){
		$scope.showRequestCancelDialog=false;
	};
	
	//Cancel Request Action. It is performed when operator confirms cancel request after stating reason for cancellation. 
	$scope.cancelRequestAction=function(){
		$scope.showRequestCancelDialog=false;
		$scope.updateChanges($scope.cancelRequestNo);
	};
	
	//Shows Dialog Box which operator tries to cancel issue request or return request of user.
	$scope.displayRequestCancelDialog=function(requestNo){
		angular.element("#requestCancelDialog").css("display","block");
		$scope.showRequestCancelDialog=true;
		$scope.cancelRequestNo=requestNo;
	};
}]);