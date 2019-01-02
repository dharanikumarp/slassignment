import React, { Component } from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";
import AUTH_CLIENT from '../Auth';
import { withRouter } from 'react-router-dom';
import { MESSAGES } from '../Urls';

class Messages extends Component {

    constructor(props) {
        super(props);
        this.state = {messages: []};
    }

    async componentDidMount() {
        const options = { 
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

        await fetch(MESSAGES, options).then((response) => {return response.json()}).then((json) => {
            this.setState({messages: json});
        });
    }

    render() {

        const columns = [
            {
                Header: 'Name',
                accessor: 'sender',
                sortable: true,
                filterable: true
            },
            {
                Header: 'Email Address',
                accessor: 'gmail',
                sortable: true,
                filterable: true
            },
            {
                Header: 'Stream Title',
                accessor: 'title',
                sortable: true,
                filterable: true      
            },
            {
                Header: 'Message',
                accessor: 'message',
                sortable: false,
                filterable: true
            },
            {
                Header: 'Date/Time',
                accessor: d => { return new Date(d.time).toLocaleString()},
                sortable: true,
                filterable: true,
                id: 'time'
            }

        ];

        return (
            <div>
                <p className='text-primary'>Persisted messages in our system. Use the textboxes below the table header to search and filter results.</p>
                {
                    this.state.messages.length === 0 && 'Loading messages...'
                }
                {
                    this.state.messages.length > 0 &&
                    <ReactTable columns={columns} data={this.state.messages} defaultPageSize={10}/>
                }
            </div>
        );
    }
}

export default withRouter(Messages);