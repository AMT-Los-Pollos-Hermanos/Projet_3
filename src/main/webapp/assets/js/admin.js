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
import {Provider} from "react-redux";
import {useInit} from "./init";

export const API_URL = 'http://localhost:8080'
export const API_KEY = '88980fa7-7167-46d5-bbe7-367a204b7bd2'
export const notyf = new Notyf()

const rootReducer = combineReducers({
    pointscales: pointscalesReducer,
    badges: badgesReducer
})

export const rootStore = configureStore({
    reducer: rootReducer
})

function App() {
    const initializeApp = useInit()
    return (
        <>
            <Provider store={rootStore}>
                <div className="d-flex justify-content-end">
                    <button className="btn btn-primary btn-lg" onClick={initializeApp}><i className="bi bi-gear"/> Initialize</button>
                </div>
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