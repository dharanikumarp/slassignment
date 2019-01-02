import React from 'react';

function RollingAverage(props) {

    return(
        <div>
            <label>Channel Momentum (Message Frequency): {props.numMessages} messages per second</label>
        </div>
    );
}

export default RollingAverage;