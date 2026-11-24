import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice";
import temaReducer from "./slices/temaSlice";

// Store única: também é lida pelos interceptors do Axios (services/api.ts).
// Store central consumida pelos componentes React e pelo interceptor Axios.
export const store = configureStore({
    reducer: {
        auth: authReducer,
        tema: temaReducer
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
