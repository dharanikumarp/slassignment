import React, { Component } from 'react';
import NavBar from './NavBar/NavBar';
import AUTH_CLIENT from './Auth';
import SecuredRoute from './SecuredRoute/SecuredRoute';
import Streams from './Streams/Streams';
import Stream from './Stream/Stream';
import UserTable from './Users/UserTable';
import Messages from './Messages/Messages';
import AllUserAndStreamStats from './Stats/AllUserAndStreamStats';

class App extends Component {
  render() {
               
    return (
      <div>
        <NavBar/>
        {!AUTH_CLIENT.isAuthenticated() &&
          <p style={{textAlign: 'center', verticalAlign: 'middle'}}>
          Welcome to StreamViewer - StreamLabs coding assignment demo to live stream with chat<br/>
          Sign-in with your Google/Youtube account to view the live streams in owned and subscribed channels.
        </p>
        }
        <SecuredRoute exact path='/streams' component={Streams}/>
        <SecuredRoute exact path='/stream' component={Stream}/>
        <SecuredRoute exact path='/users' component={UserTable}/>
        <SecuredRoute exact path='/stats' component={AllUserAndStreamStats}/>
        <SecuredRoute exact path='/messages' component={Messages}/>
      </div>
    );
  }
}

export default App;
