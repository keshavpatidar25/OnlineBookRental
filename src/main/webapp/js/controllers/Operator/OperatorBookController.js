app.controller('OperatorBookController',['$scope',function($scope){
	//Tells which link is active in left container
	$scope.selectedIndex=1;
	
	//Object containing elements present in left container(side bar) and their respective index
	$scope.leftContainerElements=[{'element':'Add Book','index':1},{'element':'Update/Delete Book','index':2}];
	
	//Which template is to be shown in main container
	$scope.bookTemplate='partials/Operator/addBook.html';
	
	//Action to be performed when index of left container is changed
	$scope.changeIndex=function(index){
		performSessionTracking("partials/Operator");
		$scope.selectedIndex=index;
		if(index==1){
			$scope.bookTemplate='partials/Operator/addBook.html';
		}else{
			$scope.bookTemplate='partials/Operator/modifyBook.html';
		}
	};
}]);