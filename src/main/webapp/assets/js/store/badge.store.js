import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {fetchConfig} from "./config.store";

export const fetchBadges = createAsyncThunk(
    'badges/fetch',
    async (_, thunkAPI) => {
        if(!thunkAPI.getState().config.fetched) {
            await thunkAPI.dispatch(fetchConfig())
        }
        const response = await axios.get(thunkAPI.getState().config.config.apiEndpoint + '/badges', {
            headers: {
                'X-API-KEY': thunkAPI.getState().config.config.apiKey
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