import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {fetchConfig} from "./config.store";

export const fetchRules = createAsyncThunk(
    'rules/fetch',
    async (_, thunkAPI) => {
        if(!thunkAPI.getState().config.fetched) {
            await thunkAPI.dispatch(fetchConfig())
        }
        const response = await axios.get(thunkAPI.getState().config.config.apiEndpoint + '/rules', {
            headers: {
                'X-API-KEY': thunkAPI.getState().config.config.apiKey
            }
        })
        return response.data
    }
)

const rulesSlice = createSlice({
    name: 'rules',
    initialState: [],
    reducers: {},
    extraReducers: {
        [fetchRules.fulfilled]: (state, action) => {
            return action.payload
        }
    }
})

export default rulesSlice.reducer