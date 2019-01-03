# slassignment
Stream Labs Assignment problem for backend developer position (Ultimately full stack developement!!!)

# Architecture
## Server
I have chosen [Java play framework](https://www.playframework.com/) for the server. 
The alternatives are **Sprint Boot** and **Apache Jersey with Tomcat/Jetty**.

## Client
I have chosen [React](https://reactjs.org/) with HTML5 and [Bootstrap](https://getbootstrap.com/). There are several alternatives including Vue, Angular.

## Database
MongoDB - One among the popular high performing document database.


### Reasons
* I have not used Spring-Boot before.
* Play is highly scalable, reactive, event driven, uses Akka and Actor based model. It comes bundled with all tools required for server development for this project including WebSockets, RESTful API support.
 * For the **Coacheeva (side project)**, I use play framework, mongoDB and Ionic (mobile). The application is live in heroku and mongolabs.
* Apache Jersey is a JAX-RS implementation. We need to bring in a server with web socket capabilities such as Tomcat/Jetty.
* I am currently using React to develop the next generation platform for [CoachingOurselves](https://www.coachingourselves.com) and Coacheeva.
* I want to further improve react framework and my Javascript skills.

###### The current CoachingOurselves platform is based on WordPress.

## Code Structure, Build and Deploy
There are two directories, one for the server and other for the client.
* For server I use this [project] (https://github.com/playframework/play-java-websocket-example) as seed and added new contents.
* For client I use [create-react-app](https://github.com/facebook/create-react-app)
* First build client using "npm run build" command. This will compile and generate an optimized build under the build directory.
* Then copy the build directory contents to the public directory of the server project.
* Then build the server using "sbt compile" followed by "sbt stage deployHeroku" if using heroku toolbelt or use heroku API key.
* The mongodb is hosted in mongolabs. Heroku does the provisioning.

I will re-list the requirements from the assignment and provide assumptions & implementation specifics wherever I feel it would help ease the understanding.

## Requirements and Solution Details

In summary this exercise requires integration of two systems YouTube(YT) which includes www.youtube.com (YT Web), https://studio.youtube.com (YT studio) & youtube mobile app (YT Mobile) with StreamViewer (SV). This is how I perceive the requirement, but I could be wrong.

I will add one more persona here to make things clear
Bob (our famous cryptography persona). Bob is not associated with SV so far (i.e he has never visited sv before), but still a fan of Natalie, wathces Natalie streams from YT Web or YT Mobile and will chat with Natalie along many other fans.

### Ambiguities
1. Should we process and display Bob's chat messages within SV? Bob will mostly chat from YT* 
1. Should we consider Bob's messages in SV while computing statistics?
1. Should we assume Natalie as a user of SV? Has Natalie ever visited and registered with SV Web app?
1. Should SV list only live broadcast of it's own registered users? Let's say if Bob is live streaming(he got excited about a new game), should Alex and Kevin be able to watch Bob's live broadcast from within SV?

I will cover how I went over these ambiguities and implementation details in the next section. In general my approach is to allow multiple options (ofcourse within the given time limit) wherever feasible controlled by a flag, a slight variation of [Feature Toggle](https://martinfowler.com/articles/feature-toggles.html)

- [x] Alex should be asked to login using his Google Account when he visits the SV website.
  * I used 'react-google-login' component on the client side and Google libraries on the server.
  * The Singleton instance [AUTH_CLIENT](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-client/src/Auth.js) is used to store the state of authentication and used across all react components.
  * Created a accesToken verifier on the server to authorize the API calls from client. Refer [AccessTokenVerifier](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-server/app/action/AccessTokenVerifierAction.java)

- [x] Alex should be presented with multiple livestreams to choose from on the home page. These are sourced from YT live api and selected/sorted based on logic you define
  * Initially I restricted the live broadcasts to a closed subscription model, whereby only registered users of SV broadcasts will be listed. So Bob's live broadcast won't be shown in the list. This also means that Natalie should be a registered user of SV and both Alex, Kevin should have subscribed to Natalie's channel.
  * What if Natalie (our active streamer) is sick? Then users visiting SV will be disappointed and will turn away. So in order to increase the DAU (daily average users), I added a feature flag to list public streams as well.
  * For now the public broadcasts are searched with term "fortnite" (my kids favourite) and limited to 12 live streams.
  * Check out [SHOW_PUBLIC_BROADCASTS](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-server/app/utils/UrlsAndConstants.java) and [LiveSteamUtil](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-server/app/utils/LiveStreamsUtil.java)


- [x] Alex should be able to click on a livestream to watch it. 
* Check out [Streams](https://github.com/dharanikumarp/slassignment/tree/master/streamviewer-client/src/Streams) and [Stream](https://github.com/dharanikumarp/slassignment/tree/master/streamviewer-client/src/Stream) react components. The Stream component further compose video player, chat and statistics.

- [x] Alex should be able to see the associated chat session while watching Natalie's stream
* TODO
* The [Stream] component/page shows the video player, chat window, rolling time summary of chat messages and user momentum in the current stream (who is sending more messages)
* [React Table](https://react-table.js.org/#/story/readme) is used to display rolling summary and user chat statistics.

- [x] Alex should be able to post messages to the chat session for the livestream he's viewing
* Chat messages are sent through websocket and in the server broadcasted to the clients watching the same live broadcast.
* Server implementation uses a modification of Actors to support chat room and broadcasting. I can say, this is my novel implementation of turning the existing Actor based flow into chat room. Akka supports broadcasting with [Graph DSL flow model](https://doc.akka.io/docs/akka/2.5/stream/stream-graphs.html), however I felt it was complex to implement in a short time.
* Refer to [Stream](https://github.com/dharanikumarp/slassignment/tree/master/streamviewer-client/src/Stream) and [Chat broadcast](https://github.com/dharanikumarp/slassignment/tree/master/streamviewer-server/app/chat) on server.

* Chat message sent from SV will also be posted in the YT*.

- [x]All messages by Kevin to Natalie's livestream chat should be stored in a persistent storage
* TODO

- [x] Alex should be able to visit the stats page

- [x] Alex should be able to see a table with usernames, message count (plus any other stats you feel like). This table should be sortable.

* The stats page uses 'React Table' component.
* This uses another web socket and the server will keep pushing the data to the client.
* The table is sortable, searchable and has filter capabilities. 'React Table' does the magic.
* This table shows the list of users, their total messages and their most active chat messages in a broadcast (i.e which stream they sent the maximum chat messages).
* The server is database query with MongoDB aggregation. Refer [ChatMessageUtil](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-server/app/utils/ChatMessageUtil.java) and [StatisticsActor](https://github.com/dharanikumarp/slassignment/blob/master/streamviewer-server/app/actors/StatisticsActor.java)

- [x] Alex should be able to search through all messages posted by Natalie's fans' usernames e.g. Kevin
* Check out the [Messages](https://github.com/dharanikumarp/slassignment/tree/master/streamviewer-client/src/Messages) react component on the client side.
* React table does the magic and the server provides the data.
* The table is sortable, searchable and include filters.

- [x] The SV webapp is mobile responsive and setup as a PWA
* I tested on couple of devices with lower form factor (an old iPad, LG-G6, iPhone 8 plus etc). I found the web application loading and working.

- [x] The stats related to chat are summarized over a rolling time window (no. messages per X seconds)
- [x] The stats are sorted, refreshed and updated frequently (so we know who creates the most hype in chat sessions in almost realtime)
* The stream page will show the chat frequency in a rolling time window for fixed intervals (1, 5, 10, 30 & 60 seconds). You can type few messages in a short span and see the table getting updated as the intervals passby. I followed the idea from the CPU graph in Ubuntu & Windows.

* The chat stats are always sorted in descending order in user with most chat messages. All the tables showing statistics and any other information are always sortable.

# Heroku App location
https://dharani-sl-assignment.herokuapp.com/

## Test Users
1. streamlabs.user1@gmail.com / slassignment
2. streamlabs.user2@gmail.com / streamlabs123


### Logistics
* I spent approximately 6 to 7 hours in total coding with debugging, fixing any issues
* For the server I spent about 3 to 4 hours and for client about 3 hours.
* React made by job easier with composable views and routing. 
* Moment I planned a layout, I was able to transform into simple views with HTML with React.
* Except for the chat window (which doesn't look very pretty, but still good looking !!!), I used standard html and components from react framework.

* I spent about 20 minutes writing this readme.
* May be another 10 minutes for the an video (upcoming)
