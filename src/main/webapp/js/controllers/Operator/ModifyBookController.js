"use strict";
//Modify(Update/Delete) Book Controller
app.controller('ModifyBookController',['$scope','$http','$filter','$timeout',function($scope,$http,$filter,$timeout){
	/* bookForReset contains all information about the book. It is used to reset changes.
	 * categoryList contains all categories of books
	 * message is used to display messages to user
	 * isErrorMessage is used to give information whether message is a success message or an error message
	 */
	$scope.bookForReset={};
	$scope.bookNoChanged=true;
	$scope.categoryList=[];
	$scope.book={bookPrice:""};
	$scope.message="";
	$scope.isErrorMessage=false;
	//Gets the list of categories
	$http.get("jspController/CategoryList.jsp").success(function(data){
		$scope.categoryList=data.categoryList;
		$scope.book.bookCategory1=$scope.categoryList[0];
	}).error(function(data,status){
		console.log("Error Occured while getting book category list. Error Code : "+status);
	});
	
	//Called when change is detected in book no. Used to disable form buttons(update, delete, reset).
	$scope.bookNoModified=function(){
		$scope.bookNoChanged=true;
	};
	
	/* Gets book details on basis of book no.
	 * If checks validity of book no.
	 * If valid then displays book details by assigning $scope.book=data.bookDetails
	 * enable form buttons(update, delete, reset)
	 */
	$scope.getBookDetails=function(){
		$http.get("jspController/Operator/GetBookDetails.jsp",{params : {jsonBook : '{"bookNo" :"'+$scope.book.bookNo+'"}'}}).success(function(data){
			if(data.result=="SUCCESS"){
				if(data.bookDetails){
					$scope.book=data.bookDetails;
					$scope.book.bookCategory1=$scope.book.bookCategory;
					$scope.message="";
					$scope.bookNoChanged=false;
				} else{
					$scope.book={};
					$scope.message="Invalid Book No.";
					$scope.isErrorMessage=true;
				}
			} else{
				$scope.book={};
				$scope.message=data.result;
				$scope.isErrorMessage=true;
			}
			$scope.bookForReset=angular.fromJson($filter('json')($scope.book));		//Deep Copying of Object
			angular.element("#imgFile").val("");
		}).error(function(data,status){
			$scope.book={};
			$scope.message="Error Occured. Please try again";
			$scope.isErrorMessage=true;
			console.log("Error Occured while getting book details. Error Code : "+status);
		});
	};
	
	//Reset to the initial values
	$scope.resetFields=function(){
		$scope.book=angular.fromJson($filter('json')($scope.bookForReset));
		angular.element("#imgFile").val("");
	};
	
	/* Delete Book
	 * Book is deleted only if Book is not issued
	 */
	$scope.deleteBook=function(){
		if(!confirm("Are you sure you want to delete this book?")){
			return;
		}
		$http.post("jspController/Operator/DeleteBook.jsp",'{"bookNo" :"'+$scope.book.bookNo+'"}').success(function(data){
			if(data.result=="SUCCESS"){
				$scope.book={};
				$scope.bookForReset={};
				$scope.bookNoChanged=true;
				$scope.message="Book Deleted Successfully";
				$scope.isErrorMessage=false;
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
    	var jsonBook=$filter('json')($scope.book);
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
    	 * (ii) Reset Book Details are changed to new details 
    	 */
    	if(resultVal=="SUCCESS"){
			$scope.message="Book Updated Successfully.";
			$scope.isErrorMessage=false;
    		$scope.bookForReset=angular.fromJson($filter('json')($scope.book));
			if($scope.book.bookCategory1=="Other"){
    			$scope.categoryList.push($scope.book.bookCategory);
    			$scope.categoryList.sort();
    		}
		} else{
			$scope.message=resultVal;
			$scope.isErrorMessage=true;
		}
 		$timeout(function(){
 			$scope.book.bookCategory1=$scope.book.bookCategory;
 		});
	};
	
	/* Updates Book Method
	 * It checks update book form entries and checks whether entries are correct or not.
	 * If new image file is selected. It reads data of Image File in Base64 Format and assign it to bookCover variable.
	 * Then update book details method is called.
	 */
	$scope.updateBook=function(){
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
		if (bookImgFile.files && bookImgFile.files[0]) {
			if(bookImgFile.files[0].size>(512*1024)){
				$scope.message="Image Size Should Not Exceed 512 KB.";
				$scope.isErrorMessage=true;
				return;
			}
	        var fr= new FileReader();
	        fr.onload = function(e) {
	        	$scope.book.bookCover = e.target.result;
	        	$scope.book.bookCover = $scope.book.bookCover.substr(22);
	        	$scope.book.bookCover=$scope.book.bookCover.replace(",", "");
	        	updateBookDetails();
	        };       
	        fr.readAsDataURL( document.getElementById("imgFile").files[0]);
		} else{
			updateBookDetails();
		}		
		angular.element("#imgFile").val("");
	};
	
}]);