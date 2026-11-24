// Utilitário de cookies disponível no projeto, mas não integrado ao fluxo de login atual (que usa Redux em memória).
import { IdentidadeAutenticada } from "../types/auth";

const COOKIE_OPTIONS = "path=/; SameSite=Lax";

export function salvarToken(token: string): void {
    document.cookie = `token=${encodeURIComponent(token)}; ${COOKIE_OPTIONS}`;
}

export function salvarSessao(token: string, usuario: IdentidadeAutenticada): void {
    salvarToken(token);
    document.cookie = `usuario=${encodeURIComponent(JSON.stringify(usuario))}; ${COOKIE_OPTIONS}`;
}

export function carregarSessao(): { token: string; usuario: IdentidadeAutenticada | null } {
    const cookies = document.cookie.split("; ");
    const tokenCookie = cookies.find((item) => item.startsWith("token="));
    const usuarioCookie = cookies.find((item) => item.startsWith("usuario="));
    const token = tokenCookie ? decodeURIComponent(tokenCookie.slice(6)) : "";

    if (!token || !usuarioCookie) {
        limparSessao();
        return { token: "", usuario: null };
    }

    try {
        const usuario = JSON.parse(decodeURIComponent(usuarioCookie.slice(8))) as IdentidadeAutenticada;
        if (typeof usuario.id === "number" && typeof usuario.role === "string"
                && typeof usuario.nome === "string" && typeof usuario.email === "string"
                && (usuario.empresaId === null || typeof usuario.empresaId === "number")) {
            return { token, usuario };
        }
    } catch {
        // Sessão inválida: remove os cookies e exige novo login.
    }

    limparSessao();
    return { token: "", usuario: null };
}

export function limparSessao(): void {
    document.cookie = "token=; Max-Age=0; path=/";
    document.cookie = "usuario=; Max-Age=0; path=/";
}
