import React, {useCallback, useEffect, useRef, useState} from 'react';
import axios from "axios";
import * as bs from 'bootstrap/dist/js/bootstrap.bundle.min'
import {notyf} from "../admin";
import {useDispatch, useSelector} from "react-redux";
import {fetchConfig} from "../store/config.store";
import {fetchRules} from "../store/rules.store";

const RuleComponent = () => {

    const rules = useSelector(s => s.rules)
    const badges = useSelector(s => s.badges)
    const ps = useSelector(s => s.pointscales)
    const config = useSelector(s => s.config)
    const [modal, setModal] = useState(null)

    const dispatch = useDispatch()

    const selectedBadgeId = useRef(null)
    const selectedPointscaleId = useRef(null)

    const [formData, setFormData] = useState({
        conditionType: 'answer',
        doRewardBadge: false,
        doRewardPoint: false,
        rewardPointscaleAmount: 20
    });

    useEffect(() => {
        setModal(new bs.Modal(document.getElementById('ruleModal')))
        dispatch(fetchRules())
    }, []);

    const deleteRule = useCallback((id) => {
        axios.delete(config.config.apiEndpoint + '/rules/' + id, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 204) {
                notyf.success('Rule deleted')
                dispatch(fetchRules())
            } else {
                notyf.error('Error while deleting the rule')
            }
        })
    }, [config])

    const createRule = useCallback((e) => {
        e.preventDefault()
        let properties
        if (formData.conditionType === 'vote') {
            properties = {
                status: formData.voteDirection,
                quantity: 1
            }
        } else {
            properties = {
                type: 'add',
                quantity: 1
            }
        }
        let then = {}
        if (formData.doRewardBadge) {
            then = {
                ...then,
                award_badge: {
                    badge_id: selectedBadgeId.current.value
                }
            }
        }
        if (formData.doRewardPoint) {
            then = {
                ...then,
                award_points: {
                    amount: formData.rewardPointscaleAmount,
                    point_scale_id: selectedPointscaleId.current.value
                }
            }
        }
        const payload = {
            if: {
                type: formData.conditionType,
                properties
            },
            then
        }
        axios.post(config.config.apiEndpoint + '/rules', payload, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Rule created')
                modal.hide()
                dispatch(fetchRules())
            } else {
                notyf.error('Error while creating the rule')
            }
        })
    }, [config])

    const handleChange = (e) => {
        const target = e.target
        const value = target.type === 'checkbox' ? target.checked : target.value
        setFormData({
            ...formData,
            [target.name]: value
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
                        <img src={badgeFound.icon} style={{maxWidth: '200px', maxHeight: '200px'}} alt=""/>
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
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Create new rule</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form onSubmit={createRule}>
                                <fieldset>
                                    <legend>Conditions</legend>
                                    <div className="mb-3">
                                        <label className="form-label">Type</label>
                                        <select className="form-control" name="conditionType"
                                                value={formData.conditionType} onChange={handleChange}>
                                            <option value="answer">Answer</option>
                                            <option value="question">Question</option>
                                            <option value="comment">Comment</option>
                                            <option value="vote">Vote</option>
                                        </select>
                                    </div>
                                    {formData.conditionType === 'vote' && (
                                        <div className="mb-3">
                                            <label className="form-label">Vote direction</label>
                                            <div className="form-check">
                                                <input className="form-check-input" type="radio" name="voteDirection"
                                                       id="up" value="UP" checked={formData.voteDirection === 'UP'}
                                                       onChange={handleChange}/>
                                                <label className="form-check-label" htmlFor="up">
                                                    UP
                                                </label>
                                            </div>
                                            <div className="form-check">
                                                <input className="form-check-input" type="radio" name="voteDirection"
                                                       id="down" value="DOWN"
                                                       checked={formData.voteDirection === 'DOWN'}
                                                       onChange={handleChange}/>
                                                <label className="form-check-label" htmlFor="down">
                                                    DOWN
                                                </label>
                                            </div>
                                        </div>
                                    )}
                                </fieldset>
                                <fieldset>
                                    <legend>Rewards</legend>
                                    <div className="form-check form-switch mb-3">
                                        <input type="checkbox" id="rewardBadge" name="doRewardBadge"
                                               className="form-check-input" checked={formData.doRewardBadge}
                                               onChange={handleChange}/><label htmlFor="rewardBadge"
                                                                               className="form-check-label">Reward
                                        badge ?</label>
                                    </div>
                                    {formData.doRewardBadge && (
                                        <div className="mb-3">
                                            <label className="form-label">Badge</label>
                                            <select className="form-control" name="rewardBadgeId" ref={selectedBadgeId}>
                                                {badges.map(badge => <option key={badge.id}
                                                                             value={badge.id}>{badge.name}</option>)}
                                            </select>
                                        </div>
                                    )}
                                    <div className="form-check form-switch mb-3">
                                        <input type="checkbox" id="rewardPoints" name="doRewardPoint"
                                               className="form-check-input" checked={formData.doRewardPoint}
                                               onChange={handleChange}/><label htmlFor="rewardPoints"
                                                                               className="form-check-label">Reward
                                        points ?</label>
                                    </div>
                                    {formData.doRewardPoint && (
                                        <>
                                            <div className="mb-3">
                                                <label className="form-label">Pointscale</label>
                                                <select className="form-control" name="rewardPointscaleId"
                                                        ref={selectedPointscaleId}>
                                                    {ps.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                                                </select>
                                            </div>
                                            <div className="mb-3">
                                                <label className="form-label">Amount</label>
                                                <input type="number" min="1" className="form-control"
                                                       name="rewardPointscaleAmount"
                                                       value={formData.rewardPointscaleAmount} onChange={handleChange}/>
                                            </div>
                                        </>
                                    )}

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