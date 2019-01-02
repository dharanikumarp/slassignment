import React, { Component } from 'react';
import './messagelist.css';

class MessageList extends Component {

    componentDidUpdate() {
        //this.el.scrollIntoView({ behavior: 'smooth' });
        this.el.scrollTop = this.el.scrollHeight;
    }

    render() {
        let breakWordStyle = {overflowWrap: 'break-word'}
        return (
            <div className='chat-messages panel panel-default' ref={el => { this.el = el; }}>                 
                {this.props.messages.map(message => {
                return (
                    <div key={message.time} className='message-container'>
                        <img src={message.profileImageUrl} alt='avatar' width='40px' height='40px'/>
                        <span style={breakWordStyle}>{message.message}</span>
                    </div>
                )
             })}
                {/*<div style={{ float:"left", clear: "both" }}
                    ref={(el) => { this.messagesEnd = el; }}>
                </div>
            */}
           </div>
        )
    }
}

export default MessageList;