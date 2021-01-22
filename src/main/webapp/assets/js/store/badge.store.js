import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {API_KEY, API_URL} from "../admin";

export const fetchBadges = createAsyncThunk(
    'badges/fetch',
    async () => {
        const response = await axios.get(API_URL + '/badges', {
            headers: {
                'X-API-KEY': API_KEY
            }
        })
        return response.data
    }
)

const badgeSlice = createSlice({
    name: 'badges',
    initialState: [],
    reducers: {},
    extraReducers: {
        [fetchBadges.fulfilled]: (state, action) => {
            return action.payload
        }
    }
})

export default badgeSlice.reducer