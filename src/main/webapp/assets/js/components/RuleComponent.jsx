import React, {useEffect, useState} from 'react';
import axios from "axios";
import * as bs from 'bootstrap/dist/js/bootstrap.bundle.min'
import {API_KEY, API_URL, notyf} from "../admin";

const RuleComponent = () => {

    const [rules, setRules] = useState([])
    const [badges, setBadges] = useState([])
    const [ps, setPs] = useState([])
    const [modal, setModal] = useState(null)

    useEffect(() => {
        fetchData()
        setModal(new bs.Modal(document.getElementById('ruleModal')))
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

    const createRule = (e) => {
        e.preventDefault()
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
            <h2>Rules <button className="btn btn-primary btn-sm" onClick={() => modal.show()}>+</button></h2>
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
            <div className="modal fade" id="ruleModal" tabIndex="-1" aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog modal-lg">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Create new rule</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form action="" onSubmit={createRule}>
                                <fieldset>
                                    <legend>Conditions</legend>
                                    <div className="mb-3">
                                        <label className="form-label">Type</label>
                                        <select className="form-control">
                                            <option value="answer">Answer</option>
                                            <option value="question">Question</option>
                                            <option value="comment">Comment</option>
                                            <option value="vote">Vote</option>
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Vote direction</label>
                                        <div className="form-check">
                                            <input className="form-check-input" type="radio" name="voteDirection"
                                                   id="up" />
                                            <label className="form-check-label" htmlFor="up">
                                                UP
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="radio" name="voteDirection"
                                                   id="down" />
                                            <label className="form-check-label" htmlFor="down">
                                                DOWN
                                            </label>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <legend>Rewards</legend>
                                    <div className="form-check mb-3">
                                        <input type="checkbox" id="rewardBadge"
                                               className="form-check-input"/><label htmlFor="rewardBadge" className="form-check-label">Reward badge ?</label>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Badge</label>
                                        <select className="form-control">
                                            <option value="answer">Answer</option>
                                            <option value="question">Question</option>
                                            <option value="comment">Comment</option>
                                            <option value="vote">Vote</option>
                                        </select>
                                    </div>
                                    <div className="form-check mb-3">
                                        <input type="checkbox" id="rewardPoints"
                                               className="form-check-input"/><label htmlFor="rewardPoints" className="form-check-label">Reward points ?</label>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Pointscale</label>
                                        <select className="form-control">
                                            <option value="answer">Answer</option>
                                            <option value="question">Question</option>
                                            <option value="comment">Comment</option>
                                            <option value="vote">Vote</option>
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Amount</label>
                                        <input type="number" min="1" className="form-control" />
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" onClick={createRule}>Save
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default RuleComponent;