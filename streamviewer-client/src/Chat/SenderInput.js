import React, { Component } from 'react';

class SenderInput extends Component {

    constructor(props) {
        super(props);
        this.state = {message: ''};
    }

    handleSubmit = () => {
         console.log('handleSubmit');
         let msg = this.state.message;
         if(msg.length) {
            this.setState({message: ''});
            this.props.onNewMessageInput(msg);
         }
    }

    render() {
        return(
            <div>
                <input type='text' onChange={e => this.setState({message: e.target.value})}
                    onKeyDown={e => { if(e.key === 'Enter') this.handleSubmit(); }} 
                    value={this.state.message} />
                <button className='btn btn-primary' onClick={this.handleSubmit}>Send</button>
            </div>
        );
    }
}

export default SenderInput;