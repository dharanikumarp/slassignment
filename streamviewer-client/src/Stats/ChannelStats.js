import React, { Component } from 'react';

class ChannelStats extends Component {

    render() {
        console.log('render ', this.props);
        let tableBody = this.props.data.map(item => {
            return(
                <tr>
                    <td>{item.videoId}</td>
                    <td>{item.numMessages}</td>
                </tr>
            );
        });
        return(
            <div>
                <table border='1'>
                    <thead>
                        <tr>
                            <th>Video</th>
                            <th>Messages/Second</th>
                        </tr>
                    </thead>
                    <tbody>
                        {tableBody}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default ChannelStats;