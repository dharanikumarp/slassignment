import React from 'react';
import './YoutubePlayer.css';

function YoutubePlayer(props) {
    console.log('props ', props);
    //const videoURL = 'https://www.youtube.com/embed/?channel=' + props.channelId;
    const videoURL = 'https://www.youtube.com/embed/' + props.videoId;
    //const videoStyle = {'width': '100%', 'height': 'auto'};
    console.log('videoURL ' + videoURL);
    return (
        <div className='iframe-container'>
            <iframe src={videoURL} frameBorder='0' width='560' height='349' title='Live streaming'></iframe>
        </div>
    );
}

export default YoutubePlayer;