import React, { Component } from 'react';
import {Link, withRouter } from 'react-router-dom';
import AUTH_CLIENT from '../Auth';
// import CURR_LIVE_STREAMS from './CurrentLiveStreams';

class Streams extends Component {

    constructor(props) {
        super(props);
        this.state = { streams: null};
    }

    async componentDidMount() {
        console.log('Streams.componentDidMount()');
        console.log('AUTH_CLIENT.getAccessToken() ', AUTH_CLIENT.getAccessToken());
        console.log('AUTH_CLIENT.getIdToken() ', AUTH_CLIENT.getIdToken());

        let options = { 
            method: 'POST',
            mode: 'cors',
            cache: 'no-cache',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + AUTH_CLIENT.getIdToken()
            },
            body: JSON.stringify({
                idToken: AUTH_CLIENT.getIdToken(),
                accessToken: AUTH_CLIENT.getAccessToken()
		    })
        };

        await fetch('http://localhost:9000/streams', options).then((response) => {return response.json()}).then((json) => {
            console.log('streams ', json);
            //CURR_LIVE_STREAMS.setStreams(json);
            // if(json.self === true) {
            //     this.setState({streams: json.liveStreams});
            // } else {
            //     this.setState({streams: json});
            // }
            
            this.setState({streams: json.liveStreams});
        })

        /*
        fetch('http://localhost:9000/streams', {
                method: 'POST',
                mode: 'cors',
                cache: 'no-cache',
                headers: {
                    'Authorization': 'Bearer ' + response.tokenId,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'accessToken': response.accessToken
                })
            }).then((resp1) => {
                console.log('Inside streams ');
                if(resp1.ok) {
                    console.log('my channels ', resp1.json());
                }
            });
            */
    }

    render() {
        return (
            <div className='container'>
                <div className='row'>
                    { 
                        this.state.streams === null && <p>Fetching Live Streams...</p>
                    }

                    {
                        this.state.streams && this.state.streams.map(stream => (

                            <div key={stream.videoId} className='col-sm-12 col-md-4 col-lg-3'>
                                <Link to={{
                                    pathname: '/stream',
                                    state: {
                                        stream: stream
                                    }
                                }}>
                                    <div className='card text-white bg-success mb-3'>
                                        <img className='card-img-top' alt='Video thumbnail url' src={stream.thumbnails.default.url}/>
                                        <div className='card-header'>Channel: {stream.channelTitle}</div>
                                        <div className='card-body'>
                                            <h4 className='card-title'>{stream.title}</h4>
                                            <p className='card-text'>{stream.description}</p>
                                        </div>
                                    </div>
                                </Link>
                            </div>    
                        ))
                    }
                </div>
            </div>
        );
    }
}

export default withRouter(Streams);