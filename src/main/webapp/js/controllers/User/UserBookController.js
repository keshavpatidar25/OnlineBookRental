app.controller('UserBookController',['$scope',function($scope){
	//Tells which link is active in left container
	$scope.selectedIndex=1;
	
	//Object containing elements present in left container(side bar) and their respective index
	$scope.leftContainerElements=[{'element':'Currently Held Books','index':1},{'element':'Book History','index':2},{'element':'Bookmark','index':3}];
	
	//Which template is to be shown in main container
	$scope.bookTemplate='partials/User/currentlyHeldBooks.html';
	
	//Action to be performed when index of left container is changed
	$scope.changeIndex=function(index){
		performSessionTracking("partials/User");
		$scope.selectedIndex=index;
		if(index==1){
			$scope.bookTemplate='partials/User/currentlyHeldBooks.html';
		}else if(index==2){
			$scope.bookTemplate='partials/User/booksHistory.html';
		}else{
			$scope.bookTemplate='partials/User/bookmark.html';
		}
	};
}]);