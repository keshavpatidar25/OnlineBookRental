/*"use strict";
app.service('WebSocketService',function(){
	var ws=this;
	ws.sessionSocket = new WebSocket("ws://localhost:8080/OnlineBookRental/sessionWebSocket/");
	ws.recievedSessionData=null;
    
	ws.sessionSocket.onopen = function(){
      console.log("Socket Message is sent...");
    };
    
    ws.sessionSocket.onmessage = function (evt) {
      ws.sessionSocket.send("");
      ws.recievedSessionData = evt.data;
      console.log("Message is received..." +evt.data);
    };
    
    ws.sessionSocket.onclose = function() { 
      console.log("Connection is closed..."); 
    };
    
    ws.sessionSocket.onerror = function(evt){
      console.log("Session Socket Error : "+evt.data);	
    };
});*/