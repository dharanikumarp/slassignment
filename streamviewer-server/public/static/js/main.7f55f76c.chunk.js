(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{24:function(e,t,a){e.exports=a(42)},30:function(e,t,a){},37:function(e,t,a){},39:function(e,t,a){},42:function(e,t,a){"use strict";a.r(t);var n=a(1),s=a.n(n),r=a(21),o=a.n(r),i=(a(30),a(45)),c=a(3),l=a(4),u=a(6),m=a(5),d=a(7),h=a(43),g=a(46),p=a(19),f=new(function(){function e(){Object(c.a)(this,e),this.getProfile=this.getProfile.bind(this),this.handleAuthentication=this.handleAuthentication.bind(this),this.signOut=this.signOut.bind(this),this.isAuthenticated=this.isAuthenticated.bind(this)}return Object(l.a)(e,[{key:"handleAuthentication",value:function(e){console.log("handleAuthentication ",e),this.idToken=e.tokenId,this.accessToken=e.accessToken,this.profile=e.profileObj}},{key:"signOut",value:function(){this.idToken=null,this.accessToken=null,this.profile=null}},{key:"getProfile",value:function(){return this.profile}},{key:"getIdToken",value:function(){return this.idToken}},{key:"getAccessToken",value:function(){return this.accessToken}},{key:"isAuthenticated",value:function(){return null!=this.idToken}}]),e}()),v="https://dharani-sl-assignment.herokuapp.com/login";var b=Object(g.a)(function(e){var t=function(t){console.log("response ",t),fetch(v,{method:"POST",mode:"cors",cache:"no-cache",headers:{"Content-Type":"application/json"},body:JSON.stringify({idToken:t.tokenId,accessToken:t.accessToken})}).then(function(a){a.ok&&(f.handleAuthentication(t),console.log("Id Token verified by server"),e.history.replace("/streams"))})};return s.a.createElement("nav",{className:"navbar navbar-dark bg-primary fixed-top"},s.a.createElement(h.a,{className:"navbar-brand",to:"/"},"StreamViewer"),f.isAuthenticated()&&s.a.createElement("div",null,s.a.createElement(h.a,{className:"navbar-brand",to:"/streams"},"Live Streams"),s.a.createElement(h.a,{className:"navbar-brand",to:"/users"},"Users"),s.a.createElement(h.a,{className:"navbar-brand",to:"/stats"},"Statistics"),s.a.createElement(h.a,{className:"navbar-brand",to:"/messages"},"Messages")),!f.isAuthenticated()&&s.a.createElement(p.GoogleLogin,{clientId:"450677404358-luoq4fsgakvfq482n0j3fbhtd93ne0ve.apps.googleusercontent.com",buttonText:"Login with Google",onSuccess:t,onFailure:t,scope:"https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube.force-ssl https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtubepartner https://www.googleapis.com/auth/youtubepartner-channel-audit profile email "}),f.isAuthenticated()&&s.a.createElement("div",null,s.a.createElement("label",{className:"mr-2 text-white"},f.getProfile().name),s.a.createElement(p.GoogleLogout,{buttonText:"Logout",onLogoutSuccess:function(){fetch("https://accounts.google.com/o/oauth2/revoke?token="+f.getAccessToken()).then(function(t){f.signOut(),e.history.replace("/")})}})))}),y=a(44);var S=Object(g.a)(function(e){var t=e.component,a=e.path;return s.a.createElement(y.a,{path:a,render:function(){return f.isAuthenticated()?s.a.createElement(t,null):(e.history.replace("/"),s.a.createElement("div",null))}})}),w=a(12),O=a.n(w),E=a(14),k=function(e){function t(e){var a;return Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).state={streams:null},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){var e=Object(E.a)(O.a.mark(function e(){var t,a=this;return O.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return t={method:"POST",mode:"cors",cache:"no-cache",headers:{"Content-Type":"application/json",Authorization:"Bearer "+f.getIdToken()},body:JSON.stringify({idToken:f.getIdToken(),accessToken:f.getAccessToken()})},e.next=3,fetch("https://dharani-sl-assignment.herokuapp.com/streams",t).then(function(e){return e.json()}).then(function(e){a.setState({streams:e.liveStreams})});case 3:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}()},{key:"render",value:function(){return s.a.createElement("div",{className:"container"},s.a.createElement("div",{className:"row"},null===this.state.streams&&s.a.createElement("p",null,"Fetching Live Streams..."),this.state.streams&&this.state.streams.map(function(e){return s.a.createElement("div",{key:e.videoId,className:"col-sm-12 col-md-4 col-lg-3"},s.a.createElement(h.a,{to:{pathname:"/stream",state:{stream:e}}},s.a.createElement("div",{className:"card text-white bg-success mb-3"},s.a.createElement("img",{className:"card-img-top",alt:"Video thumbnail url",src:e.thumbnails.default.url}),s.a.createElement("div",{className:"card-header"},"Channel: ",e.channelTitle),s.a.createElement("div",{className:"card-body"},s.a.createElement("h4",{className:"card-title"},e.title),s.a.createElement("p",{className:"card-text"},e.description)))))})))}}]),t}(n.Component),j=Object(g.a)(k),I=a(18);a(37);var N=function(e){console.log("props ",e);var t="https://www.youtube.com/embed/"+e.videoId;return console.log("videoURL "+t),s.a.createElement("div",{className:"iframe-container"},s.a.createElement("iframe",{src:t,frameBorder:"0",width:"560",height:"349",title:"Live streaming"}))};var T=function(e){return s.a.createElement("p",{className:"text-primary"},e.videoId)},A=(a(39),function(e){function t(){return Object(c.a)(this,t),Object(u.a)(this,Object(m.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidUpdate",value:function(){this.el.scrollTop=this.el.scrollHeight}},{key:"render",value:function(){var e=this,t={overflowWrap:"break-word"};return s.a.createElement("div",{className:"chat-messages panel panel-default",ref:function(t){e.el=t}},this.props.messages.map(function(e){return s.a.createElement("div",{key:e.time,className:"message-container"},s.a.createElement("img",{src:e.profileImageUrl,alt:"avatar",width:"40px",height:"40px"}),s.a.createElement("span",{style:t},e.message))}))}}]),t}(n.Component)),U=function(e){function t(e){var a;return Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).handleSubmit=function(){console.log("handleSubmit");var e=a.state.message;e.length&&(a.setState({message:""}),a.props.onNewMessageInput(e))},a.state={message:""},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this;return s.a.createElement("div",null,s.a.createElement("input",{type:"text",onChange:function(t){return e.setState({message:t.target.value})},onKeyDown:function(t){"Enter"===t.key&&e.handleSubmit()},value:this.state.message}),s.a.createElement("button",{className:"btn btn-primary",onClick:this.handleSubmit},"Send"))}}]),t}(n.Component),x=function(e){function t(){return Object(c.a)(this,t),Object(u.a)(this,Object(m.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return s.a.createElement(s.a.Fragment,null,s.a.createElement(T,{videoId:this.props.videoId}),s.a.createElement(A,{messages:this.props.messages}),s.a.createElement(U,{onNewMessageInput:this.props.onNewMessageInput}))}}]),t}(n.Component),M=a(10);a(15);var P=function(e){return s.a.createElement("div",null,s.a.createElement("p",null,"User Activity in Live Stream"),0===e.userStats.length&&"Loading User Activity statistics...",e.userStats.length>0&&s.a.createElement(M.a,{columns:[{Header:"Name",accessor:"sender",sortable:!0},{Header:"Email",accessor:"gmail",sortable:!0},{Header:"Number of Messages",accessor:"numMessages",sortable:!0}],data:e.userStats,defaultPageSize:5,pageSize:5,minRows:5}))};var H=function(e){return s.a.createElement("div",null,s.a.createElement("p",null,"Rolling Time Summary of Chat Frequency"),0===e.rollingStats.length&&"Loading Rolling statistics...",e.rollingStats.length>0&&s.a.createElement(M.a,{columns:[{Header:"Interval",accessor:"interval",sortable:!0},{Header:"Frequency",accessor:"freq",sortable:!0}],data:e.rollingStats,showPagination:!1,defaultPageSize:5,minRows:5}))},C=function(e){function t(e){var a;Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).handleNewUserMessage=function(e){if(a.ws.readyState===a.ws.OPEN){var t={type:"message",googleUserId:a.state.googleUserId,gmail:a.state.gmail,videoId:a.state.videoId,message:e,sender:a.state.sender,title:a.state.title,description:a.state.description,profileImageUrl:a.state.profileImageUrl,liveChatId:a.state.stream.liveChatId,accessToken:f.getAccessToken()};a.ws.send(JSON.stringify(t))}else console.warn("Websocket is still connecting, ignoring message")},a.sendHeartBeat=function(){if(a.ws.readyState===a.ws.OPEN){var e={type:"heartbeat",sender:a.state.sender};a.ws.send(JSON.stringify(e))}};var n=a.props.location.state.stream;return console.log("selected stream ",n),a.state={sender:f.getProfile().name,googleUserId:f.getProfile().googleId,gmail:f.getProfile().email,videoId:n.videoId,profileImageUrl:f.getProfile().imageUrl,messages:[],stream:n,title:n.title,description:n.description,numMessagesPerSecond:0,userStats:[],rollingStats:[{interval:"One Second",freq:"loading..."},{interval:"Five Seconds",freq:"loading..."},{interval:"Ten Seconds",freq:"loading..."},{interval:"Thirty Seconds",freq:"loading..."},{interval:"One Minute",freq:"loading..."}]},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){var e=this,t=this.props.location.state.stream;this.setState({stream:t}),this.setState({videoId:t.videoId}),this.ws=new WebSocket("wss://dharani-sl-assignment.herokuapp.com/chat"),this.ws.onopen=function(){if(console.log("onOpen event handler",e.ws.readyState),e.ws.readyState===e.ws.OPEN){var t={type:"join",googleUserId:e.state.googleUserId,gmail:e.state.gmail,videoId:e.state.videoId,sender:e.state.sender,profileImageUrl:e.state.profileImageUrl,title:e.state.title,description:e.state.description,liveChatId:e.state.stream.liveChatId};e.ws.send(JSON.stringify(t))}setInterval(e.sendHeartBeat,1e4)},this.ws.onmessage=function(t){var a=JSON.parse(t.data);if(console.log("received ",a),"message"===a.type)console.log("received ",a),a.videoId===e.state.videoId&&e.setState({messages:[].concat(Object(I.a)(e.state.messages),[a])});else if("heartbeat"===a.type);else if("history"===a.type)console.log("type is history"),a.history&&a.history.length>0&&(console.log("history ",a.history),e.setState({messages:[].concat(Object(I.a)(e.state.messages),Object(I.a)(a.history))}));else if("stats"===a.type){var n=a.userStats.filter(function(t){return t.videoId===e.state.videoId});n.length&&e.setState({userStats:n});var s=a.rollingStats.find(function(t){return t.videoId===e.state.videoId});if(s){var r=s.rollingSummary;e.setState({rollingStats:[{interval:"One Second",freq:""+r[0]},{interval:"Five Seconds",freq:""+r[1]},{interval:"Ten Seconds",freq:""+r[2]},{interval:"Thirty Seconds",freq:""+r[3]},{interval:"One Minute",freq:""+r[4]}]})}}}}},{key:"componentWillUnmount",value:function(){console.log("Stream.componentWillUnmount()"),this.ws.close(),clearInterval(this.sendHeartBeat)}},{key:"render",value:function(){return s.a.createElement("div",{className:"container container-fluid"},s.a.createElement("div",{className:"row"},s.a.createElement("div",{className:"col-sm-12 col-md-6 col-lg-6"},s.a.createElement(N,{channelId:this.state.stream.channelId,videoId:this.state.stream.videoId})),s.a.createElement("div",{className:"col-sm-12 col-md-6 col-lg-6"},s.a.createElement(x,{videoId:this.state.title,messages:this.state.messages,onNewMessageInput:this.handleNewUserMessage}))),s.a.createElement("div",{className:"row mt-5"},s.a.createElement("div",{className:"col-sm-12 col-md-6 col-lg-6"},s.a.createElement(H,{rollingStats:this.state.rollingStats})),s.a.createElement("div",{className:"col-sm-12 col-md-6 col-lg-6"},s.a.createElement(P,{userStats:this.state.userStats}))))}}]),t}(n.Component),L=Object(g.a)(C),q=function(e){function t(e){var a;return Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).state={users:[]},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){var e=Object(E.a)(O.a.mark(function e(){var t,a=this;return O.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return t={method:"POST",mode:"cors",cache:"no-cache",headers:{"Content-Type":"application/json",Authorization:"Bearer "+f.getIdToken()},body:JSON.stringify({idToken:f.getIdToken(),accessToken:f.getAccessToken()})},e.next=3,fetch("https://dharani-sl-assignment.herokuapp.com/users",t).then(function(e){return e.json()}).then(function(e){a.setState({users:e})});case 3:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}()},{key:"render",value:function(){return s.a.createElement("div",null,s.a.createElement("p",null,"Registered users in our system"),0===this.state.users.length&&"Loading Users...",this.state.users.length>0&&s.a.createElement(M.a,{columns:[{Header:"Name",accessor:"name",sortable:!0},{Header:"Given Name",accessor:"givenName",sortable:!0},{Header:"Email Address",accessor:"email",sortable:!0}],data:this.state.users,defaultPageSize:5}))}}]),t}(n.Component),J=Object(g.a)(q),W=function(e){function t(e){var a;return Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).state={messages:[]},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){var e=Object(E.a)(O.a.mark(function e(){var t,a=this;return O.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return t={method:"POST",mode:"cors",cache:"no-cache",headers:{"Content-Type":"application/json",Authorization:"Bearer "+f.getIdToken()},body:JSON.stringify({idToken:f.getIdToken(),accessToken:f.getAccessToken()})},e.next=3,fetch("https://dharani-sl-assignment.herokuapp.com/messages",t).then(function(e){return e.json()}).then(function(e){a.setState({messages:e})});case 3:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}()},{key:"render",value:function(){var e=[{Header:"Name",accessor:"sender",sortable:!0,filterable:!0},{Header:"Email Address",accessor:"gmail",sortable:!0,filterable:!0},{Header:"Stream Title",accessor:"title",sortable:!0,filterable:!0},{Header:"Message",accessor:"message",sortable:!1,filterable:!0},{Header:"Date/Time",accessor:function(e){return new Date(e.time).toLocaleString()},sortable:!0,filterable:!0,id:"time"}];return s.a.createElement("div",null,s.a.createElement("p",{className:"text-primary"},"Persisted messages in our system. Use the textboxes below the table header to search and filter results."),0===this.state.messages.length&&"Loading messages...",this.state.messages.length>0&&s.a.createElement(M.a,{columns:e,data:this.state.messages,defaultPageSize:10}))}}]),t}(n.Component),z=Object(g.a)(W);var B=function(e){return s.a.createElement("div",null,s.a.createElement("p",{className:"text-primary"},"User Statistics"),0===e.userStats.length&&"Loading User Stats...",e.userStats.length>0&&s.a.createElement(M.a,{columns:[{Header:"Name",accessor:"sender",sortable:!0},{Header:"Email Address",accessor:"gmail",sortable:!0},{Header:"Total Messages",accessor:"totalMessages",sortable:!0},{Header:"Most Active Stream",accessor:"mostActiveVideo",sortable:!0},{Header:"Most Active Stream - Num Messages",accessor:"mostActiveVideoNumMsgs",sortable:!0}],data:e.userStats,defaultPageSize:5}))},D=function(e){function t(e){var a;return Object(c.a)(this,t),(a=Object(u.a)(this,Object(m.a)(t).call(this,e))).state={userStats:[],streamStats:[]},a}return Object(d.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){var e=this;this.ws=new WebSocket("wss://dharani-sl-assignment.herokuapp.com/stats"),this.ws.onopen=function(){e.ws.send(JSON.stringify({ping:"test"}))},this.ws.onmessage=function(t){var a=JSON.parse(t.data);e.setState({userStats:a})}}},{key:"componentWillUnmount",value:function(){console.log("AllUserAndStreamStats.componentWillUnmount()"),this.ws.close()}},{key:"render",value:function(){return s.a.createElement(B,{userStats:this.state.userStats})}}]),t}(n.Component),F=Object(g.a)(D),R=function(e){function t(){return Object(c.a)(this,t),Object(u.a)(this,Object(m.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return s.a.createElement("div",null,s.a.createElement(b,null),!f.isAuthenticated()&&s.a.createElement("p",{style:{textAlign:"center",verticalAlign:"middle"}},"Welcome to StreamViewer - StreamLabs coding assignment demo to live stream with chat",s.a.createElement("br",null),"Sign-in with your Google/Youtube account to view the live streams in owned and subscribed channels."),s.a.createElement(S,{exact:!0,path:"/streams",component:j}),s.a.createElement(S,{exact:!0,path:"/stream",component:L}),s.a.createElement(S,{exact:!0,path:"/users",component:J}),s.a.createElement(S,{exact:!0,path:"/stats",component:F}),s.a.createElement(S,{exact:!0,path:"/messages",component:z}))}}]),t}(n.Component);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));o.a.render(s.a.createElement(i.a,null,s.a.createElement(R,null)),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(e){e.unregister()})}},[[24,2,1]]]);
//# sourceMappingURL=main.7f55f76c.chunk.js.map