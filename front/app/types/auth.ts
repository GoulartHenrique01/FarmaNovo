export interface LoginResponse {
    token: string;
}

export interface IdentidadeAutenticada {
    id: number;
    empresaId: number | null;
    nome: string;
    email: string;
    cpf: string | null;
    role: string;
    status: string | null;
}

export interface AuthState {
    usuario: IdentidadeAutenticada | null;
    token: string;
}
