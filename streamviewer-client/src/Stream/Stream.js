import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import YoutubePlayer from '../VideoPlayer/YoutubePlayer';
import Chat from '../Chat/Chat';
import AUTH_CLIENT from '../Auth';
import UserStats from '../Stats/UserStats';
import RollingTimeStatsTable from '../Stats/RollingTimeStatsTable';
import {WS_CHAT} from '../Urls';

class Stream extends Component {

    constructor(props) {
        super(props);
        const { stream } = this.props.location.state;
        console.log('selected stream ',stream);
        
        this.state = {
            sender: AUTH_CLIENT.getProfile().name,
            googleUserId: AUTH_CLIENT.getProfile().googleId,
            gmail: AUTH_CLIENT.getProfile().email,
            videoId: stream.videoId,
            profileImageUrl: AUTH_CLIENT.getProfile().imageUrl,
            messages: [],
            stream: stream,
            title: stream.title,
            description: stream.description,
            numMessagesPerSecond: 0,
            userStats: [],
            rollingStats: [
                {interval: 'One Second', freq: 'loading...'},
                {interval: 'Five Seconds', freq: 'loading...'},
                {interval: 'Ten Seconds', freq: 'loading...'},
                {interval: 'Thirty Seconds', freq: 'loading...'},
                {interval: 'One Minute', freq: 'loading...'}
            ],
        };
    }
    
    componentDidMount() {
        const { stream } = this.props.location.state;
        this.setState({stream: stream});
        this.setState({videoId: stream.videoId});
        
        this.ws = new WebSocket(WS_CHAT);
        this.ws.onopen = () => {
            console.log('onOpen event handler', this.ws.readyState);
            
            if(this.ws.readyState === this.ws.OPEN) {
                let message = {
                    type: 'join',
                    googleUserId: this.state.googleUserId,
                    gmail: this.state.gmail,
                    videoId: this.state.videoId,
                    sender: this.state.sender,
                    profileImageUrl: this.state.profileImageUrl,
                    title: this.state.title,
                    description: this.state.description,
                    liveChatId: this.state.stream.liveChatId 
                };
                this.ws.send(JSON.stringify(message));
            }
            setInterval(this.sendHeartBeat, 10000);
        };
        
        this.ws.onmessage = (evt) => {
          let received = JSON.parse(evt.data);
    
          console.log('received ', received);
          if(received.type === 'message') {
            console.log('received ', received);
            if(received.videoId === this.state.videoId) {
              this.setState({messages: [...this.state.messages, received]});
            }
          } else if(received.type === 'heartbeat') {
            
          } else if(received.type === 'history') {
              console.log('type is history');
              if(received.history && received.history.length > 0) {
                console.log('history ', received.history);
                this.setState({messages: [...this.state.messages, ...received.history]});  
              }
          } else if(received.type === 'stats') {
            let userStats = received.userStats.filter(e => e.videoId === this.state.videoId);
            if(userStats.length) {
                this.setState({userStats: userStats});
            }

            //let stats = received.stats.find(e => e.videoId === this.state.videoId);
            //this.setState({numMessagesPerSecond: stats.numMessages});

            let rollingStats = received.rollingStats.find(e => e.videoId === this.state.videoId)
            if(rollingStats) {
                let rollingSummary = rollingStats.rollingSummary;
                this.setState({rollingStats: [
                    {interval: 'One Second', freq: '' + rollingSummary[0]},
                    {interval: 'Five Seconds', freq: '' + rollingSummary[1]},
                    {interval: 'Ten Seconds', freq: '' + rollingSummary[2]},
                    {interval: 'Thirty Seconds', freq: '' + rollingSummary[3]},
                    {interval: 'One Minute', freq: '' + rollingSummary[4]}
               ]});
            }
            
            
          }
        };
      }

    componentWillUnmount() {
        console.log('Stream.componentWillUnmount()');
        this.ws.close();
        clearInterval(this.sendHeartBeat);
    }

    handleNewUserMessage = (newMessage) => {
        
        if(this.ws.readyState === this.ws.OPEN) {
            // Now send the message throught the backend API
            let message = {
                type: 'message',
                googleUserId: this.state.googleUserId,
                gmail: this.state.gmail,
                videoId: this.state.videoId,
                message: newMessage,
                sender: this.state.sender,
                title: this.state.title,
                description: this.state.description,
                profileImageUrl: this.state.profileImageUrl,
                liveChatId: this.state.stream.liveChatId,
                accessToken: AUTH_CLIENT.getAccessToken()
            };
            this.ws.send(JSON.stringify(message));
        } else {
            console.warn('Websocket is still connecting, ignoring message');
        }
    }

    sendHeartBeat = () => {
        if(this.ws.readyState === this.ws.OPEN) {
          let message = {
            'type': 'heartbeat',
            'sender': this.state.sender
          };
          this.ws.send(JSON.stringify(message));
        }
    }

    render() {
        return (
            <div className='container container-fluid'>
                <div className='row'>
                    <div className='col-sm-12 col-md-6 col-lg-6'>
                        <YoutubePlayer channelId={this.state.stream.channelId} 
                        videoId={this.state.stream.videoId}/>
                    </div>
                    <div className='col-sm-12 col-md-6 col-lg-6'>
                        <Chat videoId={this.state.title} messages={this.state.messages} onNewMessageInput={this.handleNewUserMessage}/>
                    </div>
                </div>
                <div className='row mt-5'>
                    {/*<RollingAverage numMessages={this.state.numMessagesPerSecond}/>*/}
                    <div className='col-sm-12 col-md-6 col-lg-6'>
                        <RollingTimeStatsTable rollingStats={this.state.rollingStats}/>
                    </div>

                    <div className='col-sm-12 col-md-6 col-lg-6'>
                        <UserStats userStats={this.state.userStats}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Stream);
