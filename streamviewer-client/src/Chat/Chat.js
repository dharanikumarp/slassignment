import React, { Component } from 'react';

import Title from './Title';
import MessageList from './MessageList';
import SenderInput from './SenderInput';

class Chat extends Component {

    render() {
        return (
            <React.Fragment>
                <Title videoId={this.props.videoId}/>
                <MessageList messages={this.props.messages}/>
                <SenderInput onNewMessageInput={this.props.onNewMessageInput}/>
            </React.Fragment>
        );
    }
}

export default Chat;