import React from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";

function UserStats(props) {

    const columns = [
        {
            Header: 'Name',
            accessor: 'sender',
            sortable: true
        },
        {
            Header: 'Email',
            accessor: 'gmail',
            sortable: true,
        },
        {
            Header: 'Number of Messages',
            accessor: 'numMessages',
            sortable: true
        }

    ];
    return (
        <div>
            <p>User Activity in Live Stream</p>
            {
                props.userStats.length === 0 &&
                'Loading User Activity statistics...'
            }
            {
                props.userStats.length > 0 && 
                <ReactTable columns={columns} data={props.userStats} defaultPageSize={5} pageSize={5} minRows={5}/>
            }
        </div>
    );
}

export default UserStats;