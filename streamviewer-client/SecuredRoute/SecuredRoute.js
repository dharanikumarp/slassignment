import React from 'react';
import {Route, withRouter} from 'react-router-dom';
import AUTH_CLIENT from '../Auth';

function SecuredRoute(props) {
    const {component: Component, path} = props;
    return (
        <Route path={path} render={() => {

            if(!AUTH_CLIENT.isAuthenticated()) {
                props.history.replace('/');
                //auth.signin
                return <div></div>
            }

            return <Component />
        }} />
    );
}

export default withRouter(SecuredRoute);