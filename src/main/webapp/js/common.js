"use strict";

//Shows Loading image when ajax requests starts and hides when ajax requests completes
angular.element(document).ready(function(){
  angular.element(document).ajaxStart(function(){
	  angular.element("#ajaxLoadingProcessDiv").show();
  });
  angular.element(document).ajaxStop(function(){
	  angular.element("#ajaxLoadingProcessDiv").hide();
  }); 
});

// Used to restrict to enter non numeric characters.

function restrictContact(e) {
	var key = e.keyCode ? e.keyCode : e.which;

	if (key == 8) // Key for backspace
		return true;

	if (isNaN(String.fromCharCode(key)))
		return false;

	if (key == 32) // Key for spacebar
		return false;

}

//Used to enable jquery UI tabs

function enableTabs(tabId) {
	angular.element(function() {
		angular.element("#"+tabId).tabs();
	});
}

/**Used to enable jquery UI Dialog
function enableDialog(dialogId) {
	angular.element(function() {
		angular.element("#"+dialogId).dialog({
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			},
			modal : true,
			width : 750,
			minHeight : 400
		});
	});
}
*/

//Used to display the name of user who has logged in on top right corner of web page.
function displayUserName(targetId){
	if(typeof(Storage) !== "undefined") {
		var userSessionData = angular.fromJson(localStorage.getItem("user"));
		if((userSessionData) && (userSessionData.userName)){	
			angular.element("#"+targetId).text(userSessionData.userName);
		}
    } else {
    	console.log("Sorry, your browser does not support session storage.");
    }
}

/* Used to activate date time picker of jquery UI.
 * Datepicker dateFormat option provides way to change the date format to be displayed
 */
function activateDatePicker(datepickerId){
	angular.element(function() {
	    angular.element( "#"+datepickerId).datepicker();
	    angular.element( "#"+datepickerId).datepicker( "option", "dateFormat", "dd/M/yy" );
	});
}

/* Used to restrict pressing of /, ? and \ web page
 * Used in restricting search bar of web page
 */
function restrictSearchText(e){
	var key = e.keyCode ? e.keyCode : e.which;
	if(key==47 || key==63 || key==92){ //Key for Slash(/,47) and Question Mark(?,63) and Back Slash(\,92)
		return false;
	}
}

// Used to trim trailing and leading white spaces in input and textarea
function trimWhiteSpace(){
	angular.element(function(){
		angular.element(angular.element("input, textarea").blur(function(){
			this.value=angular.element.trim(this.value);
		}));
	});
}

//Convert Date From dd/MMM/yyyy format to yyyy-MM-dd format
function convertDate(date){
    var months = {'jan': '01', 'feb': '02', 'mar': '03', 'apr': '04', 'may': '05', 'jun': '06', 'jul': '07', 'aug': '08', 'sep': '09', 'oct': '10', 'nov': '11', 'dec': '12'};
    var splitDateArr = date.split('/'); // split based on '/'
    var yyyy = splitDateArr[2] ; // assign year to variable
    var mm = months[splitDateArr[1].toLowerCase()] ; // convert month into lower and get its corresponding int value.
    var dd = splitDateArr[0] ;	//get Date part of Date
    return [yyyy,mm,dd].join('-'); // join value according to format.
};

//Perform Session Tracking using Web Sockets
function performSessionTracking(route){
	var sessionSocket = new WebSocket("ws://localhost:8080/OnlineBookRental/sessionWebSocket");
	sessionSocket.onopen = function(){
		sessionSocket.send("");
		console.log("Socket Connection Established");
    };    
    sessionSocket.onmessage = function (message) {
    	var userRole=message.data;
    	sessionSocket.close();
    	console.log("Message is received..." );
    	var userPath="partials/User";
	  	var operatorPath="partials/Operator";	
	  	if(route.substr(0,13)==userPath){ 
	  		if(!userRole || userRole!="USER"){
	  			window.location.replace("/OnlineBookRental/#/login?msg=Your session has expired");
	  		}             
	  	} else if(route.substr(0,17)==operatorPath){
	  		if(!userRole || userRole!="OPERATOR"){
	  			window.location.replace("/OnlineBookRental/#/login?msg=Your session has expired");
	  		}    
	  	}
    };
    
    sessionSocket.onclose = function() { 
    	console.log("Socket Connection is closed..."); 
    };
    
    sessionSocket.onerror = function(evt){
    	console.log("Session Socket Error : "+evt.data);	
    };
}

/*Convert Base64 String into Blob
 *@param b64Data : Base64 Data
 *@param contentType : type of Base64 content(like application/pdf or image/png or image/jpg etc.)
*/
function convertBase64ToBlob(b64Data, contentType){
	var byteCharacters = atob(b64Data);
	
	var byteNumbers = new Array(byteCharacters.length);
	for (var i = 0; i < byteCharacters.length; i++) {
	    byteNumbers[i] = byteCharacters.charCodeAt(i);
	}
	var byteArray = new Uint8Array(byteNumbers);
	return new Blob([byteArray], {type: contentType});
}