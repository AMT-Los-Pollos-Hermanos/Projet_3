import axios from "axios";
import {notyf, rootStore} from "./admin";
import {fetchBadges} from "./store/badge.store";
import {fetchPointscales} from "./store/pointscale.store";
import {useSelector} from "react-redux";

export function useInit() {

    const config = useSelector(s => s.config)

    return () => {
        if(!config.fetched) {
            return
        }

        /**
         * Pointscales
         */
        axios.post(config.config.apiEndpoint + '/pointscales', {
            name: 'Global ranking'
        }, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Pointscale created')
            }
        })

        /**
         * Badges
         */
        axios.post(config.config.apiEndpoint + '/badges', {
            name: 'First comment',
            description: 'You posted your first comment',
            icon: '/overflow/badges/badge01.png',
        }, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(config.config.apiEndpoint + '/badges', {
            name: 'First question',
            description: 'You asked your first question',
            icon: '/overflow/badges/badge02.png',
        }, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(config.config.apiEndpoint + '/badges', {
            name: 'First answer',
            description: 'You answered someone for the first time',
            icon: '/overflow/badges/badge03.png',
        }, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(config.config.apiEndpoint + '/badges', {
            name: 'First vote',
            description: 'You voted for the first time',
            icon: '/overflow/badges/badge04.png',
        }, {
            headers: {
                'X-API-KEY': config.config.apiKey,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })

        setTimeout(() => {
            rootStore.dispatch(fetchBadges())
            rootStore.dispatch(fetchPointscales())
        }, 300)
    }
}