var buttons = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");

var button = buttons.ActionButton({
  id: "mozilla-link",
  label: "Visit Mozilla",
  icon: {
    "16": "./logo.png"
  },
  onClick: handleClick
});

// Show the panel when the user clicks the button.
function handleClick(state) {
  //connectToServer();
  sidebar();
}

//Client connect to server.
function connectToServer() {
    
  var Request = require("sdk/request").Request;
  var latestTweetRequest = Request({
  url: "http://localhost:8080/citexplore.web/print.jsp",
  onComplete: function (response) {
  
  var text = response.text;
  console.log("text: ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + text);
  }
  });

  // Be a good consumer and check for rate limiting before doing more.
  Request({
    url: "http://localhost:8080/citexplore.web/print.jsp",
    onComplete: function (response) {
      if (response.text) {
        latestTweetRequest.get();
      } else {
        console.log("You have been rate limited!");
      }
    }
  }).get();
}


//sidebar
function sidebar() {
  
  var sidebar = require("sdk/ui").Sidebar({
    id: 'citeXplore',
    title: 'citeXplore',
    url: require("sdk/self").data.url("sidebar.html")
  });
  
  sidebar.show();
  
}

