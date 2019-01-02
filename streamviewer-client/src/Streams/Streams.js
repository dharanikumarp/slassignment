import React, { Component } from 'react';
import {Link, withRouter } from 'react-router-dom';
import AUTH_CLIENT from '../Auth';
import {STREAMS} from '../Urls';

class Streams extends Component {

    constructor(props) {
        super(props);
        this.state = { streams: null};
    }

    async componentDidMount() {
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

        await fetch(STREAMS, options).then((response) => {return response.json()}).then((json) => {
            this.setState({streams: json.liveStreams});
        })
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
