import React from 'react';
import { Link, withRouter } from 'react-router-dom';
import {GoogleLogin, GoogleLogout} from 'react-google-login';
import AUTH_CLIENT from '../Auth';
import {LOGIN} from '../Urls';


function NavBar(props) {

    const signOut = () => {
        fetch('https://accounts.google.com/o/oauth2/revoke?token=' + AUTH_CLIENT.getAccessToken()).then((resp) => {
            AUTH_CLIENT.signOut();
            props.history.replace('/');
        });
    };

    const googleResponse = (response) => {
        console.log('response ', response);
    
        fetch(LOGIN, {
          method: 'POST',
          mode: 'cors',
          cache: 'no-cache',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
		'idToken': response.tokenId,
		'accessToken': response.accessToken
		})
        }).then((resp) => {
          if(resp.ok) {
            AUTH_CLIENT.handleAuthentication(response);
            console.log('Id Token verified by server');
            props.history.replace('/streams');
           }
        })
        
    };

    const SCOPES ='https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube.force-ssl https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtubepartner https://www.googleapis.com/auth/youtubepartner-channel-audit profile email ';
  
    return (
        <nav className='navbar navbar-dark bg-primary fixed-top'>
            <Link className='navbar-brand' to='/'>
                StreamViewer
            </Link>

            {
                AUTH_CLIENT.isAuthenticated() && 
                <div>
                    <Link className='navbar-brand' to='/streams'>Live Streams</Link>
                    <Link className='navbar-brand' to='/users'>Users</Link>
                    <Link className='navbar-brand' to='/stats'>Statistics</Link>
                    <Link className='navbar-brand' to='/messages'>Messages</Link>
                </div>
                
            }
            {
                !AUTH_CLIENT.isAuthenticated() &&
                
                <GoogleLogin clientId='450677404358-luoq4fsgakvfq482n0j3fbhtd93ne0ve.apps.googleusercontent.com' buttonText='Login with Google' onSuccess={googleResponse} 
                onFailure={googleResponse} scope={SCOPES}/>
                
            }
            {
                
                AUTH_CLIENT.isAuthenticated() &&
                <div>
                    <label className='mr-2 text-white'>{AUTH_CLIENT.getProfile().name}</label>
                
                    <GoogleLogout buttonText="Logout"
                        onLogoutSuccess={signOut}>
                    </GoogleLogout>
                </div>
            }
        </nav>
    );
}

export default withRouter(NavBar);
