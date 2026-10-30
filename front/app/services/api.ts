'use client';

import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

api.interceptors.request.use((config) => {
    const token = document.cookie
        .split("; ")
        .find((cookie) => cookie.startsWith("token="))
        ?.slice("token=".length);

    if (token) {
        config.headers.Authorization = `Bearer ${decodeURIComponent(token)}`;
    }

    return config;
});

export default api;
