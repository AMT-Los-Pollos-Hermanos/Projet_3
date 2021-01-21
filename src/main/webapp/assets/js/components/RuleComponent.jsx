import React, {useEffect, useState} from 'react';
import axios from "axios";
import {API_KEY, API_URL, notyf} from "../admin";

const RuleComponent = () => {

    const [rules, setRules] = useState([])
    const [badges, setBadges] = useState([])
    const [ps, setPs] = useState([])

    useEffect(() => {
        fetchData()
    }, []);

    const fetchData = () => {
        axios.get(API_URL + '/rules', {
            headers: {
                'X-API-KEY': API_KEY
            }
        }).then(response => setRules(response.data))

        axios.get(API_URL + '/badges', {
            headers: {
                'X-API-KEY': API_KEY
            }
        }).then(response => setBadges(response.data))

        axios.get(API_URL + '/pointscales', {
            headers: {
                'X-API-KEY': API_KEY
            }
        }).then(response => setPs(response.data))
    }

    const deleteRule = (id) => {
        axios.delete(API_URL + '/rules/' + id, {
            headers: {
                'X-API-KEY': API_KEY,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 204) {
                notyf.success('Rule deleted')
                fetchData()
            } else {
                notyf.error('Error while deleting the rule')
            }
        })
    }

    const rulesJsx = rules.map((rule, i) => {

        let badgeImg
        if (rule.then.award_badge) {
            const badgeFound = badges.find(b => b.id === rule.then.award_badge.badge_id)
            if (badgeFound === undefined) {
                badgeImg = <i>Badge {rule.then.award_badge.badge_id} not found</i>
            } else {
                badgeImg = (
                    <div>
                        <strong>Badge:</strong><br/>
                        <img src={badgeFound.icon} style={{maxWidth: '300px', maxHeight: '300px'}} alt=""/>
                    </div>
                )
            }
        }

        let pointStr
        if (rule.then.award_points) {
            const psFound = ps.find(b => b.id === rule.then.award_points.point_scale_id)
            if (psFound === undefined) {
                pointStr = <i>Pointscale {rule.then.award_points.point_scale_id} not found</i>
            } else {
                pointStr = `${rule.then.award_points.amount} points on ${psFound.name}`
            }
        }

        return (
            <tr key={i}>
                <td>{rule.id}</td>
                <td>
                    <strong>Type:</strong> {rule.if.type}<br/>
                    <strong>Properties:</strong>
                    <ul>
                        {Object.keys(rule.if.properties).map((key, i) => {
                            return (
                                <li key={i}>{key} => {rule.if.properties[key]}</li>
                            )
                        })}
                    </ul>
                </td>
                <td>
                    {rule.then.award_points !== null && pointStr}<br/>
                    {rule.then.award_badge !== null && badgeImg}
                </td>
                <td>
                    <button className="btn btn-danger btn-sm" onClick={() => deleteRule(rule.id)}><i
                        className="bi bi-trash-fill"/>
                    </button>
                </td>
            </tr>
        )
    })

    return (
        <>
            <h2>Rules <button className="btn btn-primary btn-sm">+</button></h2>
            <table className="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Conditions</th>
                    <th>Rewards</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {rulesJsx}
                </tbody>
            </table>
        </>
    );
};

export default RuleComponent;