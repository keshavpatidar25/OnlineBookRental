"use strict";
//Mail Service
app.service('MailService',['$http','$filter',function($http,$filter){
	/* Sends Mail
	 * This is used in case of book request
	 */
	this.sendMail = function(mail,bookNo){
		var mailObj = new Object();
		mailObj.mailTo=mail.mailTo;
		mailObj.mailSubject=mail.mailSubject;
		mailObj.mailBody=mail.mailBody;
		mailObj.bookNo=bookNo;
		angular.element.ajax({
			url: 'jspController/Mail.jsp', 
			type: 'POST', 
			global: false,
			data : {jsonMail : $filter('json')(mailObj) },
			error: function(xhr){
				console.log("An error occured while sending mail using mail service : " + xhr.status + " " + xhr.statusText);
			}
		});
	};
}]);