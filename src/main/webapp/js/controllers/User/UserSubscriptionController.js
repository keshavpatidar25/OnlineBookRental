app.controller('UserSubscriptionController',['$scope',function($scope){
	//Tells which link is active in left container
	$scope.selectedIndex=1;
	
	//Object containing elements present in left container(side bar) and their respective index
	$scope.leftContainerElements=[{'element':'Active Plan','index':1},{'element':'Plan History','index':2}];
	
	//Which template is to be shown in main container
	$scope.subscriptionTemplate='partials/User/subscription.html';
	
	//Action to be performed when index of left container is changed
	$scope.changeIndex=function(index){
		performSessionTracking("partials/User");
		$scope.selectedIndex=index;
		if(index==1){
			$scope.subscriptionTemplate='partials/User/subscription.html';
		}else{
			$scope.subscriptionTemplate='partials/User/subscriptionHistory.html';
		}
	};
}]);