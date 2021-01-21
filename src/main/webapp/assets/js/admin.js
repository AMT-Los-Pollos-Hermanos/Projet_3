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

export const API_URL = 'http://localhost:8080'
export const API_KEY = '88980fa7-7167-46d5-bbe7-367a204b7bd2'
export const notyf = new Notyf()

function App() {
    return (
        <>
            <div className="row">
                <div className="col-sm-6">
                    <PointscaleComponent/>
                </div>
                <div className="col-sm-6">
                    <BadgeComponent/>
                </div>
            </div>
            <RuleComponent/>
        </>
    )
}

ReactDOM.render(<App/>, document.getElementById('root'));