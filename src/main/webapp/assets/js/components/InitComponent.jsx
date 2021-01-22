import React from 'react';
import {useInit} from "../init";

const InitComponent = () => {
    const initializeApp = useInit()
    return (
        <div className="d-flex justify-content-end">
            <button className="btn btn-primary btn-lg" onClick={initializeApp}><i className="bi bi-gear"/> Initialize</button>
        </div>
    );
};

export default InitComponent;