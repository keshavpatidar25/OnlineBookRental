"use strict";
//Search Sort Service. Used to Sort Book Results
app.service('SearchSortService',['$filter', function($filter){
	//Sort by New arrival. This is done by filtering data on basis of book arrival date coming from server in descending order.
	this.sortByNewArrival=function(booksList){
		return $filter('orderBy')(booksList,'bookDetails.bookArrival',true);
	};
	//Sort by Category. This is done by filtering data on basis of book category coming from server in ascending order.
	this.sortByCategory=function(booksList){
		return $filter('orderBy')(booksList,'bookDetails.bookCategory');
	};
	/* Sort by Most Popular. This is done by filtering data in three levels:
	 * (i)On basis of book issue count in descending order
	 * (ii)For same book issue count, then on basis of book rating in descending order
	 * (iii)For same book issue count and same book rating, then on basis of no. of users who have rated for book in descending order
	 */
	this.sortByMostPopular=function(booksList){
		var returnList = $filter('orderBy')(booksList,['bookIssueCount','bookRatingDetails.avgRating','bookRatingDetails.ratingCount'],true);
		return returnList;
	};
}]);