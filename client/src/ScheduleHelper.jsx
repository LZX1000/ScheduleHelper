// ScheduleHelper.jsx

import React, { useEffect } from 'react';

import './schedule_helper.css';

/*
    * serverStatus - shows a status update of the last server post
*/

function ScheduleHelper() {
    let [serverStatus, setServerStatus] = React.useState("")

    return (
        <div className="schedule-helper-app">
            <div className="server-status">{serverStatus}</div>
            <span>Hello!</span>
        </div>
    )
}

export default ScheduleHelper;