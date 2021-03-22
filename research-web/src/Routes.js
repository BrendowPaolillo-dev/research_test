import React from "react";
import {BrowserRouter as Router, Route, Switch, Redirect} from "react-router-dom";

import App from './App';
import Dashboard from "./Dashboard";
import Research from "./Research";
import Visualize from "./Visualize";

import { isAuthenticated } from "./services/auth";

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      isAuthenticated() ? (
        <Component {...props} />
      ) : (
        <Redirect to={{ pathname: "/", state: { from: props.location } }} />
      )
    }
  />
);

export default function Routes(){
    return(
        <Router>
            <Switch>
                <Route exact path="/" component={App}/>
                <PrivateRoute path="/dashboard" component={Dashboard} />
                <PrivateRoute path="/research" component={Research} />
                <PrivateRoute path="/visualize" component={Visualize} />
            </Switch>
        </Router>
    )
}