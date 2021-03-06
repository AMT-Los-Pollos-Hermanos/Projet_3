import React, {useCallback, useEffect, useRef, useState} from 'react'
import axios from 'axios'
import * as bs from 'bootstrap/dist/js/bootstrap.bundle.min'
import {notyf} from "../admin";
import {useDispatch, useSelector} from "react-redux";
import {fetchBadges} from "../store/badge.store";

const BadgeComponent = () => {

    const badges = useSelector(s => s.badges)
    const badgeName = useRef(null)
    const badgeDescription = useRef(null)
    const badgeIcon = useRef(null)
    const [modal, setModal] = useState(null)
    const config = useSelector(s => s.config)

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(fetchBadges())
        setModal(new bs.Modal(document.getElementById('badgeModal')))
    }, [])

    const newBadge = () => {
        badgeName.current.value = ''
        badgeDescription.current.value = ''
        badgeIcon.current.value = ''
        modal.show()
    }

    const createNewBadge = useCallback((e) => {
        e.preventDefault()
        const name = badgeName.current.value
        const description = badgeDescription.current.value
        if (name !== '') {
            const formData = new FormData()
            formData.append('icon', badgeIcon.current.files[0])
            axios.post('/overflow/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                if(res.status === 200) {
                    axios.post(config.config.apiEndpoint + '/badges', {
                        name,
                        description,
                        icon: res.data
                    }, {
                        headers: {
                            'X-API-KEY': config.config.apiKey,
                            'Content-Type': 'application/json'
                        }
                    }).then(response => {
                        if (response.status === 201) {
                            notyf.success('Badge created')
                            dispatch(fetchBadges())
                            modal.hide()
                        } else {
                            notyf.error('Error while creating the badge')
                        }
                    })
                } else {
                    notyf.error('Uploading icon failed')
                }
            })
        }
    }, [config])

    const deleteBadge = useCallback((id) => {
        axios.delete(config.config.apiEndpoint + '/badges/' + id, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 204) {
                notyf.success('Badge deleted')
                dispatch(fetchBadges())
            } else {
                notyf.error('Error while deleting the badge')
            }
        }).catch(() => {
            notyf.error('Error while deleting the badge')
        })
    }, [config])

    return (
        <>
            <h2>Badges <button className="btn btn-primary btn-sm" onClick={newBadge}>+</button></h2>
            <table className="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Icon</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {badges.map((b, i) => {
                    return (
                        <tr key={i}>
                            <td>{b.id}</td>
                            <td>{b.name}</td>
                            <td><img src={b.icon} alt="" style={{maxWidth: '200px', maxHeight: '200px'}}/></td>
                            <td>
                                <button className="btn btn-danger btn-sm" onClick={() => deleteBadge(b.id)}><i
                                    className="bi bi-trash-fill"/>
                                </button>
                            </td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className="modal fade" id="badgeModal" tabIndex="-1" aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Create new badge</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form action="" onSubmit={createNewBadge}>
                                <div className="mb-3">
                                    <label htmlFor="" className="form-label">Name</label>
                                    <input type="text" className="form-control" ref={badgeName}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="" className="form-label">Description</label>
                                    <input type="text" className="form-control" ref={badgeDescription}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="" className="form-label">Icon</label>
                                    <input type="file" className="form-control" ref={badgeIcon} />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" onClick={createNewBadge}>Save
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default BadgeComponent;