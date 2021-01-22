import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

export const fetchConfig = createAsyncThunk(
    'config/fetch',
    async () => {
        const response = await axios.get('/overflow/api')
        if(response.status === 200) {
            return {
                fetched: true,
                config: response.data
            }
        }
    }
)

const configSlice = createSlice({
    name: 'config',
    initialState: {
        fetched: false,
        config: {}
    },
    reducers: {},
    extraReducers: {
        [fetchConfig.fulfilled]: (state, action) => {
            return action.payload
        }
    }
})

export default configSlice.reducer