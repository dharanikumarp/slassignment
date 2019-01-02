import React, { Component } from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";
import AUTH_CLIENT from '../Auth';
import { withRouter } from 'react-router-dom';
import { USERS } from '../Urls';

class UserTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            users: []
        };
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

        await fetch(USERS, options).then((response) => {return response.json()}).then((json) => {
            this.setState({users: json});
        });
    }

    render() {
        const columns = [
            {
                Header: 'Name',
                accessor: 'name',
                sortable: true
            },
            {
                Header: 'Given Name',
                accessor: 'givenName',
                sortable: true
            },
            {
                Header: 'Email Address',
                accessor: 'email',
                sortable: true
            }
        ];

        return(
            <div>
                <p>Registered users in our system</p>
            {
                this.state.users.length === 0 && 'Loading Users...'
            }
            {
                this.state.users.length > 0 &&
                <ReactTable columns={columns} data={this.state.users} defaultPageSize={5}/>
            }
            </div>
        );
    }
}

export default withRouter(UserTable);