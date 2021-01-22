import React from 'react'
import ReactDOM from 'react-dom'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap/dist/js/bootstrap.bundle.min'

import {Notyf} from 'notyf'
import 'notyf/notyf.min.css'
import PointscaleComponent from "./components/PointscaleComponent";
import BadgeComponent from "./components/BadgeComponent";
import RuleComponent from "./components/RuleComponent";
import {combineReducers, configureStore} from "@reduxjs/toolkit";
import pointscalesReducer from "./store/pointscale.store";
import badgesReducer from "./store/badge.store";
import configReducer from "./store/config.store";
import rulesReducer from "./store/rules.store";
import {Provider} from "react-redux";
import InitComponent from "./components/InitComponent";

export const notyf = new Notyf()

const rootReducer = combineReducers({
    pointscales: pointscalesReducer,
    badges: badgesReducer,
    rules: rulesReducer,
    config: configReducer
})

export const rootStore = configureStore({
    reducer: rootReducer
})

function App() {
    return (
        <>
            <Provider store={rootStore}>
                <InitComponent />
                <div className="row">
                    <div className="col-sm-6">
                        <PointscaleComponent/>
                    </div>
                    <div className="col-sm-6">
                        <BadgeComponent/>
                    </div>
                </div>
                <RuleComponent/>
            </Provider>
        </>
    )
}

ReactDOM.render(<App/>, document.getElementById('root'));