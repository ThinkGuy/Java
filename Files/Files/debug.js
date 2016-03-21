function connectToServer() {
	alert("a");
	if (!window.WebSocket) {
	alert("No WebSocket.");
}

if (!window.WebSocket) {
	alert("No WebSocket.");
}

var wst = new WebSocket("ws://localhost:8080/wst");

wst.send("Send message");

wst.onmessage = function(event) { 
    console.log('Client received a message',event); 
  }; 
wst.onmessage = function(event) {
	var data = event.data;
    alert(data);
};
};


