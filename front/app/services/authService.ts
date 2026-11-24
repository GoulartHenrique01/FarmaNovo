import { IdentidadeAutenticada, LoginResponse } from "../types/auth";
import api from "./api";

// A tela de login começa aqui: envia credenciais ao POST /auth/login e recebe o token JWT.
export async function loginBack(email: string, senha: string): Promise<LoginResponse> {
    const resposta = await api.post<LoginResponse>("/auth/login", { email, senha });
    return resposta.data;
}

// Recupera o perfil autenticado após o login; o token ainda é enviado explicitamente.
export async function buscarIdentidadeAutenticada(token: string): Promise<IdentidadeAutenticada> {
    const resposta = await api.get<IdentidadeAutenticada>("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
    });
    return resposta.data;
}
