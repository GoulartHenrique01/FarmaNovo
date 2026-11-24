import api from "./api";
import { Empresa, EmpresaRequest } from "../types/empresas";

export interface CadastroEmpresaRequest extends EmpresaRequest { administradorSenha: string; }

// Cada função abaixo representa uma chamada HTTP usada pelas telas administrativas de empresas.
// O cadastro público cria a conta ROLE_USER; as demais operações exigem JWT de ROLE_ADMIN.
export async function cadastrarEmpresa(empresa: CadastroEmpresaRequest): Promise<void> {
    await api.post("/empresas", empresa);
}
export async function buscarListaEmpresas(): Promise<Empresa[]> {
    return (await api.get<Empresa[]>("/empresas")).data;
}
export async function buscarEmpresaPorId(id: number): Promise<Empresa> {
    return (await api.get<Empresa>(`/empresas/${id}`)).data;
}
export async function atualizarEmpresa(id: number, empresa: EmpresaRequest): Promise<number> {
    return (await api.put(`/empresas/${id}`, empresa)).status;
}
export async function alterarStatusEmpresa(id: number, status: "ATIVO" | "BLOQUEADO"): Promise<number> {
    return (await api.patch(`/empresas/${id}/status`, { status })).status;
}
export async function excluirEmpresa(id: number): Promise<number> {
    return (await api.delete(`/empresas/${id}/excluir`)).status;
}
