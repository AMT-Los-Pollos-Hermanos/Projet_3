import React, {useEffect, useRef, useState} from 'react'
import ReactDOM from 'react-dom'
import axios from "axios";

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap/dist/js/bootstrap.bundle.min'

import {Notyf} from 'notyf'
import 'notyf/notyf.min.css'
import PointscaleComponent from "./components/PointscaleComponent";
import BadgeComponent from "./components/BadgeComponent";

export const API_URL = 'http://localhost:8080'
export const API_KEY = '88980fa7-7167-46d5-bbe7-367a204b7bd2'
export const notyf = new Notyf()

function App() {

    const [rules, setRules] = useState([])

    useEffect(() => {
        fetchData()
    }, []);

    const fetchData = () => {
        axios.get(API_URL + '/rules', {
            headers: {
                'X-API-KEY': API_KEY
            }
        }).then(response => setRules(response.data))
    }

    return (
        <>
            <div className="row">
                <div className="col-sm-6">
                    <h2>Rules <button className="btn btn-primary btn-sm">+</button></h2>
                    <table className="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Conditions</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {rules.map((rule, i) => {
                            return (
                                <tr key={i}>
                                    <td>{rule.id}</td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </div>
                <div className="col-sm-6">
                    <PointscaleComponent/>
                </div>
                <div className="col-sm-6">
                    <BadgeComponent/>
                </div>
            </div>
        </>
    )
}

ReactDOM.render(<App/>, document.getElementById('root'));