import React from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";

function RollingTimeStatsTable(props) {

    const columns = [
        {
            Header: 'Interval',
            accessor: 'interval',
            sortable: true
        },
        {
            Header: 'Frequency',
            accessor: 'freq',
            sortable: true,
        }
    ];

    return(
        <div>
            <p>Rolling Time Summary of Chat Frequency</p>
            {
                props.rollingStats.length === 0 &&
                'Loading Rolling statistics...'
            }
            {
                props.rollingStats.length > 0 && 
                <ReactTable columns={columns} data={props.rollingStats}  showPagination={false} defaultPageSize={5} minRows={5}/>
            }
        </div>
    );
}

export default RollingTimeStatsTable;