"use strict";
//Add Book Controller
app.controller('AddBookController',['$scope','$http','$filter','$timeout', function($scope,$http,$filter,$timeout){
	/* category list : contains list of book categories
	 * book : contains the book details of new book to be added
	 * message : message to be shown to user
	 * isErrorMessage : denotes whether the message shown to user is error message or success message.  
	 */
	$scope.categoryList=[];
	$scope.book={bookPrice:""};
	$scope.message="";
	$scope.isErrorMessage=false;
	
	//Gets category list
	$http.get("jspController/CategoryList.jsp").success(function(data){
		$scope.categoryList=data.categoryList;
		$scope.book.bookCategory1=$scope.categoryList[0];
	}).error(function(data,status){
		console.log("Error Occured while getting book category list. Error Code : "+status);
	});
	
	/* Add Book Details
	 * It checks add book form entries and checks whether entries are correct or not.
	 * It reads data of Image File in Base64 Format and assign it to bookCover variable.
	 * Makes an ajax call to add book and displays success or failure message.
	 */
	$scope.addBookDetails=function(){
		if(!$scope.book.bookPrice || $scope.book.bookPrice==""){
			$scope.book.bookPrice=0;
		} else if(isNaN($scope.book.bookPrice)){
			$scope.message="Invalid Book Price";
			$scope.isErrorMessage=true;
			return;
		}
		if($scope.book.bookCategory1=="Other"){
			if(!$scope.book.newBookCategory){
				$scope.message="Please Enter New Book Category";
				$scope.isErrorMessage=true;
				return;
			}
			$scope.book.bookCategory=$scope.book.newBookCategory;
		}else{
			$scope.book.bookCategory=$scope.book.bookCategory1;
		}
		
		var bookImgFile=document.getElementById("imgFile");
		if(bookImgFile.files[0].size>(512*1024)){
			$scope.message="Image Size Should Not Exceed 512 KB.";
			$scope.isErrorMessage=true;
			return;
		}
		if (bookImgFile.files && bookImgFile.files[0]) {
	        var fr= new FileReader();
	        fr.onload = function(e) {
	        	$scope.book.bookCover = e.target.result;
	        	$scope.book.bookCover = $scope.book.bookCover.substr(22);
	        	$scope.book.bookCover = $scope.book.bookCover.replace(",", "");
	        	var jsonBook=($filter('json')($scope.book));
	        	var resultVal=null;
	        	angular.element.ajax({
	        		url:"jspController/Operator/AddBook.jsp",
	        		data:jsonBook,
	        		type:'POST',
	        		async:false,
	        		success:function(result){
	        			resultVal=angular.fromJson(result).result;
	    			},
	    			error:function(xhr){
	    				resultVal="Error Occured. Please try again";
	    				console.log("An error occured while adding book : " + xhr.status + " " + xhr.statusText);
	    			}
	        	});
	        	if(resultVal=="SUCCESS"){
        			$scope.message="Book Added Successfully.";
	    			$scope.isErrorMessage=false;
	    			//If new book category was added then insert that new category in book category combo box
	        		if($scope.book.bookCategory1=="Other"){
	        			$scope.categoryList.push($scope.book.bookCategory);
	        			$scope.categoryList.sort();
	        		}
	       
        		} else{
        			$scope.message=resultVal;
	    			$scope.isErrorMessage=true;
        		}
	     		$scope.$apply();
	     		$timeout(function(){
	     			$scope.book.bookCategory1=$scope.book.bookCategory;
	     		});
	        };       
	        fr.readAsDataURL(bookImgFile.files[0]);
	    }
		
	};
}]);