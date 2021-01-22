import axios from "axios";
import {API_KEY, API_URL, notyf, rootStore} from "./admin";
import {fetchBadges} from "./store/badge.store";
import {fetchPointscales} from "./store/pointscale.store";

export function useInit() {
    return () => {
        /**
         * Pointscales
         */
        axios.post(API_URL + '/pointscales', {
            name: 'Global ranking'
        }, {
            headers: {
                'X-API-KEY': API_KEY,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Pointscale created')
            }
        })
        axios.post(API_URL + '/pointscales', {
            name: 'Secondary ranking'
        }, {
            headers: {
                'X-API-KEY': API_KEY,
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
        axios.post(API_URL + '/badges', {
            name: 'First comment',
            description: 'You posted your first comment',
            icon: '/overflow/badges/badge01.png',
        }, {
            headers: {
                'X-API-KEY': API_KEY,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(API_URL + '/badges', {
            name: 'First question',
            description: 'You asked your first question',
            icon: '/overflow/badges/badge02.png',
        }, {
            headers: {
                'X-API-KEY': API_KEY,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(API_URL + '/badges', {
            name: 'First answer',
            description: 'You answered someone for the first time',
            icon: '/overflow/badges/badge03.png',
        }, {
            headers: {
                'X-API-KEY': API_KEY,
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status === 201) {
                notyf.success('Badge created')
            }
        })
        axios.post(API_URL + '/badges', {
            name: 'First vote',
            description: 'You voted for the first time',
            icon: '/overflow/badges/badge04.png',
        }, {
            headers: {
                'X-API-KEY': API_KEY,
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