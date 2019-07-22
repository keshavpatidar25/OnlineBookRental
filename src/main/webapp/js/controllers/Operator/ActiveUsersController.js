"use strict";
//Active Users Controller
app.controller('ActiveUsersController', ['$scope','$filter','$q','ngTableParams','SubscriptionService',function($scope,$filter,$q,ngTableParams,SubscriptionService){
	$scope.activeUsersList={};
	//Calls get active users method of Subscription Service
	SubscriptionService.getActiveUsers().success(function(data){
		
		$scope.activeUsersList=data;
		if($scope.activeUsersList.result=='SUCCESS'){
			//Ng-Table Section Start
			$scope.activeUsersTable = new ngTableParams({
		        page: 1,            // show first page
		        count: 10,          // count per page
		        filter: {
					userName:'',
					userEmail:'',
					planId:''
				},
				sorting: {
					'user.userName':'asc'
				}
				
		    }, {
		        total: $scope.activeUsersList.activeUsers.length, // length of data
		        getData: function($defer, params) {
		        	//Enable Sorting Feature of Ng-Table
		        	var orderedData = params.sorting() ?
		                    $filter('orderBy')($scope.activeUsersList.activeUsers, params.orderBy()) :
		                    $scope.activeUsersList.activeUsers;
		            
		         // Enable Filter Feature of Ng-Table
		            var orderedData =$filter('filter')(
										$filter('filter')(
											$filter('filter')(orderedData,
											{user:{userName:params.filter().userName}}),
								{user:{userEmail:params.filter().userEmail}}),
							{planId:params.filter().planId});
		            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
		        }
		    });
			//Ng-Table Section End
		} else{
			console.log("ERROR : "+$scope.activeUsersList.result);
		}
	}).error(function(data,status){
		console.log("Error Occured while getting active users list. Error Code : "+status);
	});
	
	//Gives Combo Box Filter in Plan Id column of active user's table
	$scope.planIdComboBox = function() {
        var def = $q.defer(),
        status = [{id:'',title:'ALL'},{id:'BASIC',title:'BASIC'},{id:'SILVER',title:'SILVER'},{id:'GOLD',title:'GOLD'}];
        def.resolve(status);
        return def;
    };
}]);