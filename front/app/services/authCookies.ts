import Cookies from "js-cookie";
import { IdentidadeAutenticada } from "../types/auth";

const COOKIE_OPTIONS = { expires: 7, path: "/", sameSite: "lax" as const };

export function salvarToken(token: string): void {
    Cookies.set("token", token, {
        ...COOKIE_OPTIONS,
        secure: window.location.protocol === "https:"
    });
}

export function salvarSessao(token: string, usuario: IdentidadeAutenticada): void {
    salvarToken(token);
    Cookies.set("usuario", JSON.stringify(usuario), {
        ...COOKIE_OPTIONS,
        secure: window.location.protocol === "https:"
    });
}

export function carregarSessao(): { token: string; usuario: IdentidadeAutenticada | null } {
    const token = Cookies.get("token") ?? "";
    const usuarioCookie = Cookies.get("usuario");

    if (!usuarioCookie || !token) {
        limparSessao();
        return { token: "", usuario: null };
    }

    try {
        const usuario: unknown = JSON.parse(usuarioCookie);
        if (typeof usuario === "object" && usuario !== null) {
            const dados = usuario as Record<string, unknown>;
            if (typeof dados.id === "number"
                    && (dados.usuarioId === null || typeof dados.usuarioId === "number")
                    && (dados.empresaId === null || typeof dados.empresaId === "number")
                    && typeof dados.nome === "string"
                    && typeof dados.email === "string"
                    && (dados.cpf === null || typeof dados.cpf === "string")
                    && typeof dados.role === "string"
                    && (dados.status === null || typeof dados.status === "string")) {
                return {
                    token,
                    usuario: {
                        id: dados.id,
                        usuarioId: dados.usuarioId,
                        empresaId: dados.empresaId,
                        nome: dados.nome,
                        email: dados.email,
                        cpf: dados.cpf,
                        role: dados.role,
                        status: dados.status
                    }
                };
            }
        }
    } catch (error) {
        console.error("Não foi possível restaurar a sessão salva.", error);
    }

    console.error("A sessão salva está incompleta e foi removida.");
    limparSessao();
    return { token: "", usuario: null };
}

export function limparSessao(): void {
    Cookies.remove("token", { path: "/" });
    Cookies.remove("usuario", { path: "/" });
}
