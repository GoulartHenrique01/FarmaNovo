import { AuthState, IdentidadeAutenticada } from "@/app/types/auth";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// Token JWT e dados do usuário ficam somente na memória da store (sem cookies/localStorage).
const initialState : AuthState = {
    usuario: null,
    token: ""
}

const authSlice = createSlice({
    name : "auth",
    initialState,
    reducers:{
        login:(state, action: PayloadAction<{token:string,usuario:IdentidadeAutenticada}>)=>{
            state.token = action.payload.token;
            state.usuario = action.payload.usuario;
        },
        logout:(state)=>{
            state.token = "";
            state.usuario = null;
        },
    },
});

export const {login, logout} = authSlice.actions;
export default authSlice.reducer;
