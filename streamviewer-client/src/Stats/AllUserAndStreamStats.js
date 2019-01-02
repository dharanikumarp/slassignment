import React, { Component } from 'react';
import {withRouter} from 'react-router-dom';
import AllUserStats from './AllUserStats';

import { WS_STATS } from '../Urls';

class AllUserAndStreamStats extends Component {

    constructor(props) {
        super(props);
        this.state = {
            userStats: [],
            streamStats: []
        };
    }

    componentDidMount() {
        this.ws = new WebSocket(WS_STATS);
        this.ws.onopen = () => {
            this.ws.send(JSON.stringify({ping: 'test'}))
        };
        
        this.ws.onmessage = (evt) => {
            let received = JSON.parse(evt.data);
            this.setState({userStats: received});
        }
    }

    componentWillUnmount() {
        console.log('AllUserAndStreamStats.componentWillUnmount()');
        this.ws.close();
    }
    
    render() {
        return (
            <AllUserStats userStats={this.state.userStats}/>
        );
    }
}

export default withRouter(AllUserAndStreamStats);