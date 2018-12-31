# slassignment
Stream Labs Assignment problem for backend developer position (Ultimately full stack developement!!!)

#Architecture
## Server
I have chosen [Java play framework](https://www.playframework.com/) for the server. 
The alternatives are **Sprint Boot** and **Apache Jersey with Tomcat/Jetty**.

### Reasons
* I have not used Spring-Boot before.
* Play is highly scalable, reactive, event driven, uses Akka and Actor based model. It comes bundled with all tools required for server development
this project including WebSockets, RESTful API support.
 * Also for my **Coacheeva (side project)**, I use play framework and it's is live in heroku.
* Apache Jersey is a JAX-RS implementation. We need to bring in a server with web socket capabilities such as Tomcat/Jetty.

## Client
I have chosen [React](https://reactjs.org/). I am currently using React to develop the next generation platform for 
[CoachingOurselves](https://www.coachingourselves.com) and Coacheeva. 
###### The current CoachingOurselves platform is based on WordPress.

I will list the requirements from the assignment description and provide assumptions & implementation specifics (if any).
