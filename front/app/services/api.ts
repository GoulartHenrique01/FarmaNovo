'use client';

// FLUXO HTTP DO FRONTEND: todas as telas chamam este cliente; ele aponta para a API e injeta o JWT do Redux.
import axios from "axios";
import { store } from "../redux/store";
import { logout } from "../redux/slices/authSlice";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

// Requisição: injeta o token JWT que está na store do Redux.
api.interceptors.request.use((config) => {
    const token = store.getState().auth.token;

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

// Resposta: 401 (token expirado/inválido) faz logout no Redux e o layout volta para o login.
api.interceptors.response.use(
    (resposta) => resposta,
    (erro) => {
        const url: string = erro?.config?.url ?? "";
        if (erro?.response?.status === 401 && !url.includes("/auth/login")) {
            store.dispatch(logout());
        }
        return Promise.reject(erro);
    }
);

export default api;
