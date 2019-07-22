"use strict";
//Fetch Books Service
app.service('FetchBooksService',['$http','$filter', function($http,$filter){
	
	/**this.guestBookSearch = function(filterString){
		return $http.get('jspController/SearchByAll.jsp',{params : {jsonFilter : '{"filter":"'+filterString+'"}' }});
		
		
	};*/
	
	// Sends ajax request to search books on basis of all(title, author and category)
	this.bookSearchByAll = function(filterString){
		/*return $http.get('jspController/SearchByAll.jsp',{params : {jsonFilter : '{"filter":"'+filterString+'"}' }});*/
		return $http.jsonp('/OnlineBookRental/rest/books/'+filterString+"?callback=JSON_CALLBACK");
	};
	
	// Sends ajax request to search books on basis of title
	this.bookSearchByTitle = function(filterString){
		/*return $http.get('jspController/SearchByTitle.jsp',{params : {jsonFilter : '{"filter":"'+filterString+'"}' }});*/
		return $http.jsonp('/OnlineBookRental/rest/books/title/'+filterString+"?callback=JSON_CALLBACK");
	};
	
	// Sends ajax request to search books on basis of author
	this.bookSearchByAuthor = function(filterString){
		/*return $http.get('jspController/SearchByAuthor.jsp',{params : {jsonFilter : '{"filter":"'+filterString+'"}' }});*/
		return $http.jsonp('/OnlineBookRental/rest/books/author/'+filterString+"?callback=JSON_CALLBACK");
	};

	// Sends ajax request to search books on basis of category
	this.bookSearchByCategory = function(filterString){
		/*return $http.get('jspController/SearchByCategory.jsp',{params : {jsonFilter : '{"filter":"'+filterString+'"}' }});*/
		return $http.jsonp('/OnlineBookRental/rest/books/category/'+filterString+"?callback=JSON_CALLBACK");
	};
	
	/* Get Issued Books
	 * Just gets only book no of books issued by user
	 */
	this.getIssuedBooks = function(){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user!=null){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/IssuedBooks.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {	
					resultObject = angular.fromJson(data);
				}, error: function(xhr){
					console.log("An error occured while getting issued books using fetch books service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
	
	/* Get Issued Books Details
	 * Just gets all book details of books issued by user
	 */
	this.getIssuedBooksDetails = function(){
		var resultObject={};
		var user = localStorage.getItem("user");
		if(user!=null){
			user = angular.fromJson(user);
			angular.element.ajax({url: 'jspController/User/CurrentlyHeldBooks.jsp', async:false, type: 'GET', data : {jsonUser : '{"userEmail" : "' + user.userEmail+ '"}'}, 
				success : function(data) {	
					resultObject = angular.fromJson(data);
				}, error: function(xhr){
					console.log("An error occured while getting issued books details using fetch books service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
	
	/* Get User's Book History
	 * Convert from Date & to Date to "yyyy-MM-dd" format
	 * Check whether fromDate is greater than to Date
	 * If less than then Get book history
	 */
	this.getBooksHistory = function(fromDate,toDate){
		var resultObject={booksHistoryList:[],result:""};
		fromDate = convertDate(fromDate);
		toDate = convertDate(toDate);
		if(new Date(fromDate)>new Date(toDate)){
			resultObject.result="From Date cannot be greater than to Date";
			alert("From Date cannot be greater than to Date");
			return resultObject;
		}
		var user = localStorage.getItem("user");
		if(user!=null){
			var booksHistory = new Object();
			user = angular.fromJson(user);
			booksHistory.userEmail=user.userEmail;
			booksHistory.fromDate=fromDate;
			booksHistory.toDate=toDate;
			var jsonBooksHistory = $filter('json')(booksHistory);
			angular.element.ajax({url: 'jspController/User/BooksHistory.jsp', async:false, type: 'GET', data : {jsonBooksHistory : jsonBooksHistory}, 
				success : function(data) {	
					resultObject = angular.fromJson(data);
				}, error: function(xhr){
					console.log("An error occured while getting books history using fetch books service : " + xhr.status + " " + xhr.statusText);
				}
			});
		}
		return resultObject;
	};
}]);