import React from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";

function AllUserStats(props) {

    const columns = [
        {
            Header: 'Name',
            accessor: 'sender',
            sortable: true
        },
        {
            Header: 'Email Address',
            accessor: 'gmail',
            sortable: true,
        },
        {
            Header: 'Total Messages',
            accessor: 'totalMessages',
            sortable: true
        },
        {
            Header: 'Most Active Stream',
            accessor: 'mostActiveVideo',
            sortable: true,
        },
        {
            Header: 'Most Active Stream - Num Messages',
            accessor: 'mostActiveVideoNumMsgs',
            sortable: true
        }
    ];
    return(
        <div>
            <p className='text-primary'>User Statistics</p>
            {
                props.userStats.length === 0 && 'Loading User Stats...'
            }
            {
                props.userStats.length > 0 &&
                <ReactTable columns={columns} data={props.userStats} defaultPageSize={5}/>
            }
        </div>
    );
}

export default AllUserStats;