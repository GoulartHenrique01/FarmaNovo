import { Usuario } from "./usuario"

// Formato esperado da resposta de autenticação retornada pela API.
export interface LoginResponse{
    token:string
}

export interface AuthState{
    usuario: Usuario|null,
    token: String
}