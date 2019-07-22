app.controller('ManageRequestController',['$scope',function($scope){
	//Tells which link is active in left container
	$scope.selectedIndex=1;
	
	//Which template is to be shown in main container
	$scope.requestTemplate='partials/Operator/modifyRequest.html';
	
	//Object containing elements present in left container(side bar) and their respective index
	$scope.leftContainerElements=[{'element':'Request Status','index':1},{'element':'Active Users','index':2}];
	
	//Action to be performed when index of left container is changed
	$scope.changeIndex=function(index){
		performSessionTracking("partials/Operator");
		$scope.selectedIndex=index;
		if(index==1){
			$scope.requestTemplate='partials/Operator/modifyRequest.html';
		}else{
			$scope.requestTemplate='partials/Operator/activeUsers.html';
		}
	};
}]);