import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {API_KEY, API_URL} from "../admin";

export const fetchPointscales = createAsyncThunk(
    'pointscales/fetch',
    async () => {
        const response = await axios.get(API_URL + '/pointscales', {
            headers: {
                'X-API-KEY': API_KEY
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