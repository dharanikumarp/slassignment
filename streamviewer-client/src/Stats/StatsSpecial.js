import React, { Component } from 'react';
import ReactTable from "react-table";
import "react-table/react-table.css";

class Stats extends Component {

    render() {
        const data = this.props.data;
        const columns = [
            {
                Header: 'Video Id',
                accessor: 'videoId'
            },
            {
                Header: 'Number of Messages / 5 Seconds',
                accesor: 'numMessages'
            }
        ];

        return(
            <div>
                <ReactTable data={data} columns={columns} showPagination={false} showPageJump={false} defaultPageSize={5}/>
            </div>
        );
    }
}

export default Stats;