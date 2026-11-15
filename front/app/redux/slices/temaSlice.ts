import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// Preferência de tema (claro/escuro) compartilhada pelo sistema.
const temaSlice = createSlice({
    name: "tema",
    initialState: { escuro: false },
    reducers: {
        definirTema: (state, action: PayloadAction<boolean>) => {
            state.escuro = action.payload;
        },
        alternarTema: (state) => {
            state.escuro = !state.escuro;
        },
    },
});

export const { definirTema, alternarTema } = temaSlice.actions;
export default temaSlice.reducer;
