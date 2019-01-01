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
* I want to master react framework.

###### The current CoachingOurselves platform is based on WordPress.

## Code Structure, Build and Deploy
There are two directories, one for the server and other for the client.
* For server I use this [project] (https://github.com/playframework/play-java-websocket-example) as seed and added new contents.
* For client I use [create-react-app](https://github.com/facebook/create-react-app)
* First build client using "npm run build" command. This will compile and generate an optimized build under the build directory.
* Then copy the build directory contents to the public directory of the server project.
* Then build the server using "sbt compile" followed by "sbt stage deployHeroku" if using heroku toolbelt or use heroku API key.

I will list the requirements from the assignment description and provide assumptions & implementation specifics (if any).
