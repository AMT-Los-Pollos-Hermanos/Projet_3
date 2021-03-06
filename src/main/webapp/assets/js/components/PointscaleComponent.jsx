import React, {useCallback, useEffect, useRef, useState} from 'react'
import axios from 'axios'
import * as bs from 'bootstrap/dist/js/bootstrap.bundle.min'
import {notyf} from "../admin";
import {useDispatch, useSelector} from "react-redux";
import {fetchPointscales} from "../store/pointscale.store";

const PointscaleComponent = () => {

    const pointscales = useSelector(s => s.pointscales)
    const dispatch = useDispatch()
    const pointscaleName = useRef(null)
    const [psModal, setPsModal] = useState(null)

    const config = useSelector(s => s.config)

    useEffect(() => {
        setPsModal(new bs.Modal(document.getElementById('pointscaleModal')))
        dispatch(fetchPointscales())
    }, [])

    const newPointscale = () => {
        pointscaleName.current.value = ''
        psModal.show()
    }

    const createNewPointscale = useCallback((e) => {
        e.preventDefault()
        const name = pointscaleName.current.value
        if (name !== '') {
            axios.post(config.config.apiEndpoint + '/pointscales', {
                name
            }, {
                headers: {
                    'X-API-KEY': config.config.apiKey,
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (response.status === 201) {
                    notyf.success('Pointscale created')
                    dispatch(fetchPointscales())
                    psModal.hide()
                } else {
                    notyf.error('Error while creating the pointscale')
                }
            })
        }
    }, [config])

    const deletePointscale = useCallback((id) => {
        axios.delete(config.config.apiEndpoint + '/pointscales/' + id, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 204) {
                notyf.success('Pointscale deleted')
                dispatch(fetchPointscales())
                psModal.hide()
            } else {
                notyf.error('Error while deleting the pointscale')
            }
        })
    }, [config])

    return (
        <>
            <h2>Pointscales <button className="btn btn-primary btn-sm" onClick={newPointscale}>+</button></h2>
            <table className="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {pointscales.map((ps, i) => {
                    return (
                        <tr key={i}>
                            <td>{ps.id}</td>
                            <td>{ps.name}</td>
                            <td className="d-flex gap-1">
                                <button className="btn btn-danger btn-sm" onClick={() => deletePointscale(ps.id)}><i
                                    className="bi bi-trash-fill"/>
                                </button>
                            </td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className="modal fade" id="pointscaleModal" tabIndex="-1" aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Create new pointscale</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form action="" onSubmit={createNewPointscale}>
                                <label htmlFor="" className="form-label">Name</label>
                                <input type="text" className="form-control" ref={pointscaleName}/>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" onClick={createNewPointscale}>Save
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default PointscaleComponent;