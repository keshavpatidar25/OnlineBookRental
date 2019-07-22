"use strict";
app.controller('ModifyBookDialogController',['$scope','$http','$filter','$timeout','ngDialog',function($scope,$http,$filter,$timeout,ngDialog){
	/* bookToModifyForReset contains all information about the book. It is used to reset changes.
	 * categoryList contains all categories of books
	 * message is used to display messages to user
	 * isErrorMessage is used to give information whether message is a success message or an error message
	 */
	$scope.bookToModifyForReset={};
	$scope.categoryList=[];
	$scope.message="";
	$scope.isErrorMessage=false;
	//Gets the list of categories
	$http.get("jspController/CategoryList.jsp").success(function(data){
		$scope.categoryList=data.categoryList;
		$timeout(function(){
			$scope.bookToModify.bookCategory1=$scope.bookToModify.bookCategory;
			$scope.bookToModifyForReset=angular.fromJson($filter('json')($scope.bookToModify));
		});
	}).error(function(data,status){
		console.log("Error Occured while getting book category list. Error Code : "+status);
	});
	
	//Reset to the initial values
	$scope.resetFields=function(){
		$scope.bookToModify=angular.fromJson($filter('json')($scope.bookToModifyForReset));
		angular.element("#imgFile").val("");
	};
	
	/* Delete Book
	 * Book is deleted only if Book is not issued
	 */
	$scope.deleteBook=function(){
		if(!confirm("Are you sure you want to delete this book?")){
			return;
		}
		$http.post("jspController/Operator/DeleteBook.jsp",'{"bookNo" :"'+$scope.bookToModify.bookNo+'"}').success(function(data){
			if(data.result=="SUCCESS"){
				$scope.bookToModify={};
				$scope.bookToModifyForReset={};
				$scope.booksList.splice($scope.ngDialogData.modifiedBookIndex,1);
				$scope.message="";
				alert("Book Deleted Successfully.");
				ngDialog.closeAll();
			} else{
				$scope.message=data.result;
				$scope.isErrorMessage=true;
			}
		}).error(function(data,status){
			$scope.message="Error Occured. Please try again";
			$scope.isErrorMessage=true;
			console.log("Error Occured while deleting book. Error Code : "+status);
		});
	};
	
	/* Updates Book Details
	 * Makes ajax request to update book details
	 */
	var updateBookDetails=function(){
    	var jsonBook=$filter('json')($scope.bookToModify);
    	var resultVal=null;
    	angular.element.ajax({
    		url:"jspController/Operator/UpdateBook.jsp",
    		data:jsonBook,
    		type:'POST',
    		async:false,
    		success:function(result){
        		resultVal=angular.fromJson(result).result;
			},
			error:function(xhr){
				resultVal="Error Occured. Please try again";
				console.log("An error occured while updating book details : " + xhr.status + " " + xhr.statusText);
			}
    	});
    	/* If book is successfully updated
    	 * (i) Success message is shown to user
    	 * (ii) Changes are reflected on book details on search page
    	 * (iii) Reset Book Details are changed to new details 
    	 */
    	if(resultVal=="SUCCESS"){
			$scope.message="Book Updated Successfully.";
			$scope.isErrorMessage=false;
			$scope.booksList[$scope.ngDialogData.modifiedBookIndex].bookDetails=angular.fromJson($filter('json')($scope.bookToModify));
    		$scope.bookToModifyForReset=angular.fromJson($filter('json')($scope.bookToModify));
			if($scope.bookToModify.bookCategory1=="Other"){
    			$scope.categoryList.push($scope.bookToModify.bookCategory);
    			$scope.categoryList.sort();
    		}
		} else{
			$scope.message=resultVal;
			$scope.isErrorMessage=true;
		}
 		$timeout(function(){
 			$scope.bookToModify.bookCategory1=$scope.bookToModify.bookCategory;
 		});
	};
	
	/* Updates Book Method
	 * It checks update book form entries and checks whether entries are correct or not.
	 * If new image file is selected. It reads data of Image File in Base64 Format and assign it to bookCover variable.
	 * Then update book details method is called.
	 */
	$scope.updateBook=function(){
		if(!$scope.bookToModify.bookPrice || $scope.bookToModify.bookPrice==""){
			$scope.bookToModify.bookPrice=0;
		} else if(isNaN($scope.bookToModify.bookPrice)){
			$scope.message="Invalid Book Price";
			$scope.isErrorMessage=true;
			return;
		}
		if($scope.bookToModify.bookCategory1=="Other"){
			if(!$scope.bookToModify.newBookCategory){
				$scope.message="Please Enter New Book Category";
				$scope.isErrorMessage=true;
				return;
			}
			$scope.bookToModify.bookCategory=$scope.bookToModify.newBookCategory;
		}else{
			$scope.bookToModify.bookCategory=$scope.bookToModify.bookCategory1;
		}
		
		var bookImgFile=document.getElementById("imgFile");		
		if (bookImgFile.files && bookImgFile.files[0]) {
			if(bookImgFile.files[0].size>(512*1024)){
				$scope.message="Image Size Should Not Exceed 512 KB.";
				$scope.isErrorMessage=true;
				return;
			}
	        var fr= new FileReader();
	        fr.onload = function(e) {
	        	$scope.bookToModify.bookCover = e.target.result;
	        	$scope.bookToModify.bookCover = $scope.bookToModify.bookCover.substr(22);
	        	$scope.bookToModify.bookCover=$scope.bookToModify.bookCover.replace(",", "");
	        	updateBookDetails();
	        };       
	        fr.readAsDataURL( document.getElementById("imgFile").files[0]);
		} else{
			updateBookDetails();
		}		
		angular.element("#imgFile").val("");
	};
	
}]);