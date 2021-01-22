import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {fetchConfig} from "./config.store";

export const fetchPointscales = createAsyncThunk(
    'pointscales/fetch',
    async (_, thunkAPI) => {
        if(!thunkAPI.getState().config.fetched) {
            await thunkAPI.dispatch(fetchConfig())
        }
        const response = await axios.get(thunkAPI.getState().config.config.apiEndpoint + '/pointscales', {
            headers: {
                'X-API-KEY': thunkAPI.getState().config.config.apiKey
            }
        })
        return response.data

    }
)

const pointscaleSlide = createSlice({
    name: 'pointscales',
    initialState: [],
    reducers: {},
    extraReducers: {
        [fetchPointscales.fulfilled]: (state, action) => {
            return action.payload
        }
    }
})

export default pointscaleSlide.reducer